package com.tts.opergit;

import java.io.File;

import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.SubmoduleUpdateCommand;
import org.eclipse.jgit.submodule.SubmoduleStatus;

/**
 * 项目名称：  gitoper   
 * 类名称：  Commit   
 * 描述：      commit操作
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:29:48 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Commit {
	
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
                    
                    //切换branch  head处于游离态  相当于更新子库的master分支代码 提交
                    
                    Branch.switchBranch(newPath + "\\" +s.getKey(), "master");
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

}
