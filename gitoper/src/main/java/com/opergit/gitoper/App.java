package com.opergit.gitoper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.apache.http.impl.client.BasicCredentialsProvider;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteListCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.api.RmCommand;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.SubmoduleInitCommand;
import org.eclipse.jgit.api.SubmoduleUpdateCommand;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.errors.UnsupportedCredentialItem;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.submodule.SubmoduleStatus;
import org.eclipse.jgit.transport.CredentialItem;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * 项目名称：  gitoper   
 * 类名称：  App   
 * 描述：  
 * @author  gaocheng   
 * 创建时间：  2017年5月31日 下午1:15:27 
 * 修改人：gaocheng    修改日期： 2017年5月31日
 * 修改备注：
 *
 */
public class App {

	//git地址
	private static String gitUrl = "https://github.com/wuyuleiliu/gitoper.git";
	
	//git账号
	private static String username = "792564728@qq.com";
//	private static String username = "gaocheng";
	
	//git账号密码
	private static String password = "gaocheng328";
	
	//本地仓库地址
	private static String localUrl = "e:\\git\\gaocheng";
		
	//切换分支名
	private static String branchName = "remotes/origin/";
	
	//需要提交的文件名，多文件以逗号分隔,为空默认所有文件add
	private static String commitFile = "";
	
	//存储提交版本信息（push）
	private static Map<String,String> pushMes = new HashMap<String, String>();
	
	//commit的文件名
	private static String fileNames;
	
	/** 
	 * 方法名：  main 
	 * 描述：      主方法入口
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:57:54
	 * @param args
	 * @throws Exception
	 *
	 */
	public static void main(String[] args) throws Exception {
		
		//执行交互
		while(true) {
			interact();
		}
			
	}

	
	/** 
	 * 方法名：  interact 
	 * 描述：      执行交互入口
	 * @author  gaocheng   
	 * 创建时间：2017年6月2日 上午9:15:56
	 *
	 */
	private static void interact() {
		
		System.out.println("请选择需要执行的操作：");
		System.out.println("1.clone仓库");
		System.out.println("2.pull操作");
		System.out.println("3.commit操作");
		System.out.println("4.push操作(包含commit)");
		System.out.println("5.remove操作");
		System.out.println("6.回退到某个版本代码（revert）");
		System.out.println("7.回退到某个版本代码（reset）");
		System.out.println("8.查看仓库所有的分支");
		System.out.println("9.查看提交版本纪录");
		System.out.println("10.切换分支");		
		System.out.println("11.合并当前分支");		
		System.out.println("12.退出");
				
		Scanner s = new Scanner(System.in);
		int i = s.nextInt();
		
		try {
			switch (i) {
				case 1:
					//下载仓库（主仓和子仓）,默认为master
					cloneRepository(gitUrl,localUrl,"");
					cloneSubRepository(localUrl);
					break;
				case 2:
					//pull操作（主仓和子仓）		
					pullRepository(localUrl);
					pullSubRepository(localUrl);
					System.out.println("pull完成");
					break;
				case 3:
					//commit操作（主仓+子仓） 注意  子仓库只有是自己的才能进行远程仓库更新操作
					System.out.println("请输入此次提交的信息（区分版本）");
					String mes = s.next();
					commitRepository(localUrl, fileNames, mes);

					if(getSonRepo(localUrl) != null && !"".equals(getSonRepo(localUrl))) {
						commitSubRepository(localUrl, "", mes);
					}

					break;
				case 4:
					//更新远程仓库的代码  空文件夹提交无效
					System.out.println("请输入此次提交的信息（区分版本）");
					String mesP = s.next();
					updateRepository(localUrl, commitFile, mesP);

					break;
				case 5:
					//删除操作
					System.out.println("请输入删除的文件（文件需写明后缀,多文件以逗号分隔）:");
					String file = s.next();
					removeRepository(localUrl, file);
					break;
				case 6:
					//得到版本信息
					log(localUrl);
					//恢复代码版本
					String ver1 = s.next();
					if(!"".equals(ver1) && ver1 != null) {
						rollBackRevertCode(ver1);
					}
					break;
				case 7:
					//得到版本信息
					log(localUrl);
					//恢复代码版本
					String ver2 = s.next();
					if(!"".equals(ver2) && ver2 != null) {
						rollBackResetCode(ver2);
					}
					break;
				case 8:
					//得到远程仓库的分支
					getRemoteBranchNames(localUrl, "origin");
					break;
				case 9:
					//查看提交版本信息
					log(localUrl);
					break;
				case 10:
					//切换分支
					System.out.println("请输入想要切换的分支（如master）:");
					String br = s.next();
					switchBranch(localUrl,branchName+br);
					break;
				case 11:
					//合并当前分支
					
					break;
				case 12:
					//退出
					System.exit(0);
					break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	/** 
	 * 方法名：  rollBackResetCode 
	 * 描述：       reset恢复代码版本
	 * @author  gaocheng   
	 * 创建时间：2017年6月2日 下午12:41:06
	 * @param ver2
	 *
	 */
	private static void rollBackResetCode(String version) {
		try {
			
			Git git = Git.open(new File(localUrl));
			ResetCommand resetCommand = git.reset();
			
			RevCommit rev = null;
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			//根据SHA-1来决定回退到哪个版本
			for (RevCommit revCommit : list) {
				if(pushMes.get(version).equals(revCommit.getName())){
					rev = revCommit;
				}

			}
			
			resetCommand.setRef(rev.getName());
			
			//reset类型
			resetCommand.setMode(ResetCommand.ResetType.HARD);
			resetCommand.call();
			System.out.println("本地仓库完成恢复！");
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}


	/** 
	 * 方法名：  rollBackRevertCode 
	 * 描述：      revert恢复代码版本
	 * @author  gaocheng   
	 * 创建时间：2017年6月1日 下午6:28:34
	 *
	 */
	private static void rollBackRevertCode(String version) {
		
		try {
			
			Git git = Git.open(new File(localUrl));
			RevertCommand revertCommand = git.revert();
			
			RevCommit rev = null;
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			//根据SHA-1来决定回退到哪个版本
			for (RevCommit revCommit : list) {
				if(pushMes.get(version).equals(revCommit.getName())){
					rev = revCommit;
				}

			}
			
			revertCommand.include(rev);
			revertCommand.call();
			System.out.println("本地仓库完成恢复！");
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}


	/** 
	 * 方法名：  removeRepository 
	 * 描述：      执行git删除的相关操作    git rm test    git commit -a -m "xinxi"   git   push
	 * 		 子仓库的删除还未实现
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 下午8:26:31
	 * @param localUrl2
	 * @param removeFile2
	 *
	 */
	private static String removeRepository(String localUrl, String removeFile) {
		
		try {
			Git git = Git.open(new File(localUrl));
			RmCommand rmCommand = git.rm();
			
			String[] fileArr = removeFile.split(",");
            for (String file : fileArr) {
            	if(file != null && !"".equals(file)) {
            		rmCommand.addFilepattern(file);
            	}           		
            }
			 
			rmCommand.call();
			updateRepository(localUrl, "", "");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}


	/** 
	 * 方法名：  updateRepository 
	 * 描述：  将add commit push 操作整合      类似 git add .   git commit -a -m "xinxi"   git push
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 下午6:56:23
	 * @throws GitAPIException 
	 * @throws IOException 
	 *
	 */
	private static void updateRepository(String localPath, String fileNames, String message) throws IOException, GitAPIException {
		
		//commit操作（主仓+子仓） 注意  子仓库只有是自己的才能进行远程仓库更新操作
		commitRepository(localPath, fileNames, message);
		if(getSonRepo(localPath) != null && !"".equals(getSonRepo(localPath))) {
			commitSubRepository(localPath, "", message);
		}
		
		
		//push操作（主仓+子仓）注意  子仓库只有是自己的才能进行远程仓库更新操作
		pushRepository(localPath);
		if(getSonRepo(localPath) != null && !"".equals(getSonRepo(localPath))) {
			pushSubRepository(localPath);
			
			//更新主仓下的子仓
			//在线更新子仓
//			addSonRepo(localPath);
		}
		
	}

	/** 
	 * 方法名：  addSonRepo 
	 * 描述：       增加子仓  子仓需要先下载下来 添加到主仓中 
	 * @author  gaocheng   
	 * 创建时间：2017年6月1日 下午5:15:09
	 * @param localPath
	 * @throws IOException 
	 *
	 */
	private static void addSonRepo(String localPath) {
		


		
	}


	/** 
	 * 方法名：  log 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:18
	 * @param localPath
	 *
	 */
	public static void log(String localPath) {
		try {
			int i = 1;
					
			Git git = Git.open(new File(localPath));
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			for (RevCommit revCommit : list) {
				System.out.print(revCommit.getAuthorIdent().getWhen());
				System.out.print("	" + revCommit.getName());
				System.out.print("	" + "纪录"+ i);
				System.out.println("	" + revCommit.getShortMessage());

				
				pushMes.put(String.valueOf(i), revCommit.getName());					
				i++;

			}
			
		} catch (Exception e) {

		}
	}

	/** 
	 * 方法名：  getChangedFiles 
	 * 描述：       查询仓库状态      git status
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:28
	 * @param localPath
	 *
	 */
	public static void getChangedFiles(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			StatusCommand statusCommand = git.status();
			org.eclipse.jgit.api.Status status = statusCommand.call();
			Map<String, Set<String>> result = new HashMap<String, Set<String>>();
			Set<String> addedSet = status.getAdded();
			result.put("added", addedSet);
			Set<String> changedSet = status.getChanged();
			result.put("changed", changedSet);
			Set<String> missSet = status.getMissing();
			result.put("missed", missSet);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/** 
	 * 方法名：  fetchBranch 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:40
	 * @param localPath
	 * @return
	 *
	 */
	public static String fetchBranch(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			git.fetch().call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


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
	 * 方法名：  getRemotes 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:59:31
	 * @param localPath
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 *
	 */
	public static List<String> getRemotes(String localPath) throws IOException, GitAPIException {
		Git git = Git.open(new File(localPath));
		RemoteListCommand remoteListCommand = git.remoteList();
		List<RemoteConfig> list = remoteListCommand.call();
		List<String> result = new LinkedList<String>();
		for (RemoteConfig remoteConfig : list) {
			result.add(remoteConfig.getName());
		}
		return result;
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
	 * 方法名：  pushRepository 
	 * 描述：       push到远程仓库
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:00:09
	 * @param localPath
	 * @return
	 *
	 */
	public static String pushRepository(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			PushCommand pushCommand = git.push();
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username,
					password);
			pushCommand.setCredentialsProvider(credentialsProvider);
			// pushCommand.add(fileNames);
			pushCommand.setForce(true).setPushAll();
			Iterable<PushResult> iterable = pushCommand.call();
			for (PushResult pushResult : iterable) {
				System.out.println(pushResult.toString());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  pushSubRepository 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:00:18
	 * @param localPath
	 * @param sub
	 * @return
	 *
	 */
	public static String pushSubRepository(String localPath) {
		try {
			String newPath = localPath + "\\.git\\modules";
			
			Git gitP = Git.open(new File(localPath));
			//根据status获取对应的子仓文件夹名称
			if(gitP.submoduleStatus().call().entrySet() != null) {
    			for (java.util.Map.Entry<String, SubmoduleStatus> s:gitP.submoduleStatus().call().entrySet()) {
                    Git gitIner = Git.open(new File(newPath + "\\" +s.getKey()));
                    //切换branch  headc处于游离态
                    switchBranch(newPath + "\\" +s.getKey(), "master");
                    
                    PushCommand pushCommand = gitIner.push();
        			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username,
        					password);
        			pushCommand.setCredentialsProvider(credentialsProvider);
        			pushCommand.setForce(true).setPushAll();
        			Iterable<PushResult> iterable = pushCommand.call();
        			for (PushResult pushResult : iterable) {
        				System.out.println(pushResult.toString());
        			}
    			}
			}

			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  commitRepository 
	 * 描述：       提交一个或多个文件或文件夹
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:00:40
	 * @param localPath
	 * @param fileNames
	 * @param message
	 * @return
	 *
	 */
	public static String commitRepository(String localPath, String fileNames, String message) {
		try {
			Git git = Git.open(new File(localPath));
			AddCommand addCommand = git.add();
			
			if("".equals(fileNames) || fileNames == null) {
				
				//git add .
				addCommand.addFilepattern(".");    
			} else {
				String[] fileArr = fileNames.split(",");
				for (String file : fileArr) {
					addCommand.addFilepattern(file);
				}
			}
			
			addCommand.call();
			CommitCommand commitCommand = git.commit();
			commitCommand.setMessage(message);
			commitCommand.setAllowEmpty(true);
			commitCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  commitSubRepository 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:00:48
	 * @param localPath
	 * @param sub
	 * @param fileNames
	 * @param message
	 * @return
	 *
	 */
	public static String commitSubRepository(String localPath, String fileNames, String message) {
		try {
			String newPath = localPath + "\\.git\\modules";
			
			Git gitP = Git.open(new File(localPath));
			//根据status获取对应的子仓文件夹名称 子仓库存在时才进行操作
			if(gitP.submoduleStatus().call().entrySet() != null) {
    			for (java.util.Map.Entry<String, SubmoduleStatus> s:gitP.submoduleStatus().call().entrySet()) {
                    Git gitIner = Git.open(new File(newPath + "\\" +s.getKey()));
                    
                    //切换branch  headc处于游离态  相当于更新子库的master分支代码 提交
                    
                    switchBranch(newPath + "\\" +s.getKey(), "master");
                    AddCommand addCommand = gitIner.add();
    
        			addCommand.addFilepattern(".");
        			addCommand.call();
        			CommitCommand commitCommand = gitIner.commit();
        			commitCommand.setMessage(message);
        			commitCommand.setAllowEmpty(true);
        			commitCommand.call();
    
    			}
			}
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  pullRepository 
	 * 描述：      pull操作    类似 git pull
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:01:01
	 * @param localPath
	 * @return
	 *
	 */
	public static String pullRepository(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			PullCommand pullCommand = git.pull();
			pullCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  pullSubRepository 
	 * 描述：      子仓pull操作  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:01:12
	 * @param localPath
	 * @param sub
	 * @return
	 *
	 */
    public static String pullSubRepository(String localPath) {
        try {
            String newPath = localPath + "\\.git\\modules";
	
			Git git = Git.open(new File(localPath));
			//根据status获取对应的子仓文件夹名称
			for (java.util.Map.Entry<String, SubmoduleStatus> s:git.submoduleStatus().call().entrySet()) {
                Git gitIner = Git.open(new File(newPath + "\\" +s.getKey()));
                
                PullCommand pullCommand = gitIner.pull();
                //切换branch  headc处于游离态

                switchBranch(newPath + "\\" +s.getKey(), "master");
                
                pullCommand.call();
			}

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }



	/** 
	 * 方法名：  cloneRepository 
	 * 描述：       下载主仓       //类似  git clone https://github.com/wuyuleiliu/Test.git
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 下午5:06:07
	 * @param url         git地址
	 * @param localPath   本地仓库路径
	 * @param branch      分支
	 * @return
	 *
	 */
	public static String cloneRepository(String url, String localPath, String branch) {
		try {
			File path = new File(localPath);
			if (path.exists()) {
				deleteDir(path);
			}
			System.out.println("开始下载主仓。。。");
			CloneCommand cloneCommand = Git.cloneRepository().setURI(url);
			
			//gitlab账号必须提供认证
			cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
			
			if("".equals(branch) || branch == null ) {
				cloneCommand.setDirectory(new File(localPath)).call();

			} else {
				cloneCommand.setDirectory(new File(localPath)).setBranch(branch).call();
			}
			
			System.out.println("主仓下载完成。。。");
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}


	/** 
	 * 方法名：  cloneSubRepository 
	 * 描述：       子仓下载      git submodule init          git submodule update      
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:01:32
	 * @param localPath   本地仓库路径
	 * @return
	 *
	 */
	public static String cloneSubRepository(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			
			List<String> subFile = new ArrayList<String>();
			System.out.println("开始下载子仓。。。");
			SubmoduleInitCommand submoduleInit = git.submoduleInit();
			SubmoduleUpdateCommand submoduleUpdate = git.submoduleUpdate();
			
			//gitlab账号必须提供认证
			submoduleUpdate.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
			
			
			
			//根据status获取对应的子仓文件夹名称
			for (java.util.Map.Entry<String, SubmoduleStatus> s:git.submoduleStatus().call().entrySet()) {
				subFile.add(s.getKey());
			}
			
			if(subFile != null){
				
				for (String s : subFile) {
					System.out.println("准备下载子仓：" + s);
					submoduleInit.addPath(s);
					submoduleUpdate.addPath(s);
					
				}
				submoduleInit.call();
				submoduleUpdate.call();
				System.out.println("子仓下载完成。。。");
				;
			}
			
			return "";
			
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	/** 
	 * 方法名：  deleteDir 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午11:01:42
	 * @param dir
	 * @return
	 *
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
	

	/** 
	 * 方法名：  getSonRepo 
	 * 描述：      获取仓库下的子仓 以逗号分隔
	 * @author  gaocheng   
	 * 创建时间：2017年6月1日 下午3:53:10
	 * @param localUrl    主仓路径
	 * @return
	 * @throws IOException 
	 * @throws GitAPIException 
	 *
	 */
	private static String getSonRepo(String localUrl) throws IOException, GitAPIException{
		Git git = Git.open(new File(localUrl));
		String sonFile = "";
		//根据status获取对应的子仓文件夹名称
		for (java.util.Map.Entry<String, SubmoduleStatus> s:git.submoduleStatus().call().entrySet()) {
			sonFile += s.getKey() + ","; 	
		}
		return sonFile;
	}
}
