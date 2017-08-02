package com.tts.opergit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 * 项目名称：  gitoper   
 * 类名称：  Log   
 * 描述：       log操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:48:13 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Log {
	/** 
	 * 方法名：  log 
	 * 描述：  
	 * @author  gaocheng   
	 * 创建时间：2017年5月31日 上午10:58:18
	 * @param localPath
	 * @param isdisplay  是否显示日志
	 *
	 */
	public static Map<String,String> log(String localPath, boolean isdisplay) {
		Map<String,String> mes = new HashMap<String, String>(); 
		try {
			int i = 1;		
			Git git = Git.open(new File(localPath));
			LogCommand logCommand = git.log();

			Iterable<RevCommit> list = logCommand.call();
			for (RevCommit revCommit : list) {
				if(isdisplay){
    				System.out.print(revCommit.getAuthorIdent().getWhen());
    				System.out.print("	" + revCommit.getName());
    				System.out.print("	" + "纪录"+ i);
    				System.out.println("	" + revCommit.getShortMessage());
				}
				
				mes.put(String.valueOf(i), revCommit.getName());					
				i++;

			}
			

		} catch (Exception e) {
			e.getStackTrace();
		}
		return mes;
	}
}
