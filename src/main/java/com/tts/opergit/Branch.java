package com.tts.opergit;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;

/**
 * 项目名称：  gitoper   
 * 类名称：  Branch   
 * 描述：      分支操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:25:47 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Branch {
	/** 
	 * 方法名：  switchBranch 
	 * 描述：        git branch testgc    git checkout testgc 
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:50
	 * @param localPath
	 * @param branch
	 * @return
	 *
	 */
	public static String switchBranch(String localPath, String branch) {
		try {
			Git git = Git.open(new File(localPath));
			String newBranch = branch.substring(branch.lastIndexOf("/") + 1, branch.length());
			CheckoutCommand checkoutCommand = git.checkout();
			List<String> list = getLocalBranchNames(localPath);
			if (!list.contains(newBranch)) {// 如果本地分支
				checkoutCommand.setStartPoint(branch);
				checkoutCommand.setCreateBranch(true);
			}
			checkoutCommand.setName(newBranch);
			checkoutCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  getCurrentBranch 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:59:13
	 * @param localPath
	 * @return
	 * @throws IOException
	 *
	 */
	public static String getCurrentBranch(String localPath) throws IOException {
		Git git = Git.open(new File(localPath));
		return git.getRepository().getBranch();
	}


	/** 
	 * 方法名：  getCurrentRemoteBranch 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:59:22
	 * @param localPath
	 * @return
	 * @throws IOException
	 *
	 */
	public static String getCurrentRemoteBranch(String localPath) throws IOException {
		Git git = Git.open(new File(localPath));
		StoredConfig storedConfig = git.getRepository().getConfig();
		String currentRemote = storedConfig.getString("branch", getCurrentBranch(localPath), "remote");
		return currentRemote;
	}


	/** 
	 * 方法名：  switchSubhBranch 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:57
	 * @param localPath
	 * @param sub
	 * @param branch
	 * @return
	 *
	 */
	public static String switchSubhBranch(String localPath, String sub, String branch) {
		try {
			Git git = Git.open(new File(localPath + "\\.git\\modules" + sub));
			String newBranch = branch.substring(branch.lastIndexOf("/") + 1, branch.length());
			CheckoutCommand checkoutCommand = git.checkout();
			List<String> list = getLocalBranchNames(localPath + "\\.git\\modules" + sub);
			if (!list.contains(newBranch)) {// 如果本地分支
				checkoutCommand.setStartPoint(branch);
				checkoutCommand.setCreateBranch(true);
				checkoutCommand.setForce(true);
			}
			checkoutCommand.setName(newBranch);
			checkoutCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/** 
	 * 方法名：  getLocalBranchNames 
	 * 描述：       获取本地仓库的所有分支名     git branch
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:59:43
	 * @param localPath
	 * @return
	 * @throws IOException
	 *
	 */
	public static List<String> getLocalBranchNames(String localPath) throws IOException {
		List<String> result = new LinkedList<String>();
		Git git = Git.open(new File(localPath));
		Map<String, Ref> map = git.getRepository().getAllRefs();

		Set<String> keys = map.keySet();
		for (String str : keys) {
			if (str.indexOf("refs/heads") > -1) {
				String el = str.substring(str.lastIndexOf("/") + 1, str.length());
				result.add(el);
				System.out.println(el);
			}
		}
		return result;
	}


	/** 
	 * 方法名：  getRemoteBranchNames 
	 * 描述：       获取远程仓库中的所有分支名     git branch -a
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:59:51
	 * @param localPath
	 * @param remote
	 * @return
	 * @throws IOException
	 *
	 */
	public static List<String> getRemoteBranchNames(String localPath, String remote) throws IOException {
		List<String> result = new LinkedList<String>();
		Git git = Git.open(new File(localPath));
		Map<String, Ref> map = git.getRepository().getAllRefs();
		Set<String> keys = map.keySet();
		String index = "refs/remotes/" + remote;
		for (String str : keys) {
			if (str.indexOf(index) > -1) {
				// String el=str.substring(str.lastIndexOf("/")+1, str.length());
				result.add(str);
				System.out.println(str);
			}
		}
		return result;
	}
	
	/** 
	 * 方法名：  mergeBranch 
	 * 描述：      合并分支到当前分支    git merge <name>
	 * @author  gaocheng   
	 * 创建时间：2017年6月5日 上午8:36:23
	 * @param branchName   分支名
	 *
	 */
	public static void mergeBranch(String localPath, String message, String branchName) {
		try {
			
			Git git = Git.open(new File(localPath));
			MergeCommand mergeCommand = git.merge();
			
			Ref aCommit = git.getRepository().findRef(branchName);
			mergeCommand.include(aCommit);
			mergeCommand.setFastForward(MergeCommand.FastForwardMode.FF);
			//自动提交
			mergeCommand.setCommit(true);
			mergeCommand.setMessage(message);

			
			mergeCommand.call();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
}
