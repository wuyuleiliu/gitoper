package com.tts.opergit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.submodule.SubmoduleStatus;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.tts.consts.Global;

/**
 * 项目名称：  gitoper   
 * 类名称：  Push   
 * 描述：     push操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:32:24 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Push {
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

			pushCommand.setCredentialsProvider(Global.credentialsProvider);
			// pushCommand.add(fileNames);
			// git push --all --force origin
			//如果远程主机的版本比本地版本更新，推送时Git会报错，要求先在本地做git pull合并差异，然后再推送到远程主机。这时，如果你一定要推送，可以使用–force选项
			pushCommand.setForce(true);
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
                    //切换branch  head处于游离态
                    Branch.switchBranch(newPath + "\\" +s.getKey(), "master");
                    
                    PushCommand pushCommand = gitIner.push();
        			pushCommand.setCredentialsProvider(Global.credentialsProvider);
        			pushCommand.setForce(true);
//        			Iterable<PushResult> iterable = pushCommand.call();
//        			for (PushResult pushResult : iterable) {
//        				System.out.println(pushResult.toString());
//        			}
    			}
			}

			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
