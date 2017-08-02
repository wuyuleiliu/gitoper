package com.tts.opergit;

import java.io.File;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * 项目名称：  gitoper   
 * 类名称：  Reset   
 * 描述：      reset操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午11:14:30 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Reset {
	
	/** 
	 * 方法名：  rollBackResetCode 
	 * 描述：       reset恢复代码版本
	 * @author  gaocheng   
	 * 创建时间：2017年6月2日 下午12:41:06
	 * @param ver2
	 * @param localUrl
	 * @param mes
	 *
	 */
	public static void rollBackResetCode(String version, String localUrl,  Map<String,String> mes) {
		try {
			
			Git git = Git.open(new File(localUrl));
			ResetCommand resetCommand = git.reset();

			RevCommit rev = null;
			LogCommand logCommand = git.log();
			Iterable<RevCommit> list = logCommand.call();
			//根据SHA-1来决定回退到哪个版本
			for (RevCommit revCommit : list) {
				if(mes.get(version).equals(revCommit.getName())){
					rev = revCommit;
				}

			}
			//git reset --hard 32232
			resetCommand.setRef(rev.getName());
			
			//reset类型  hard
			resetCommand.setMode(ResetCommand.ResetType.HARD);
			resetCommand.call();
			System.out.println("本地仓库完成恢复！");
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
	}
}
