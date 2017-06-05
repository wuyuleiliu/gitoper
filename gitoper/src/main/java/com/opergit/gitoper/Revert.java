package com.opergit.gitoper;

import java.io.File;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.RevertCommand;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * 项目名称：  gitoper   
 * 类名称：  Revert   
 * 描述：      revert操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:55:22 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Revert {
	

	/** 
	 * 方法名：  rollBackRevertCode 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年6月5日 上午10:59:19
	 * @param version  版本号
	 * @param localUrl  仓库地址 
	 * @param mes log日志信息
	 *
	 */
	public static void rollBackRevertCode(String version, String localUrl, Map<String,String> mes) {
		
		try {
			
			Git git = Git.open(new File(localUrl));
			RevertCommand revertCommand = git.revert();
			
			RevCommit rev = null;
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			//根据SHA-1来决定回退到哪个版本
			for (RevCommit revCommit : list) {
				if(mes.get(version).equals(revCommit.getName())){
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
}
