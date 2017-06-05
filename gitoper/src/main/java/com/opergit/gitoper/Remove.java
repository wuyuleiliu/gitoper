package com.opergit.gitoper;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RmCommand;

/**
 * 项目名称：  gitoper   
 * 类名称：  Remove   
 * 描述：      remove操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:34:49 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Remove {
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
	public static String removeRepository(String localUrl, String removeFile) {
		
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
			
			//commit
//			Commit.commitRepository(localUrl, "", "");
//			Commit.commitSubRepository(localUrl, "", "");
			
			//push
//			Push.pushRepository(localUrl, "", "");
//			Push.pushSubRepository(localUrl, "", "");

			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
	}

}
