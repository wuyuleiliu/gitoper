package com.tts.opergit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.submodule.SubmoduleStatus;

import com.tts.consts.Global;

/**
 * 项目名称：  gitoper   
 * 类名称：  Pull   
 * 描述：      pull操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:23:47 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Pull {
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
	
			pullCommand.setCredentialsProvider(Global.credentialsProvider);
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
                pullCommand.setCredentialsProvider(Global.credentialsProvider);
                //切换branch  head处于游离态
                Branch.switchBranch(newPath + "\\" +s.getKey(), "master");
                
                pullCommand.call();
			}

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
