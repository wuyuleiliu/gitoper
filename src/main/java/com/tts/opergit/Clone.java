package com.tts.opergit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.SubmoduleInitCommand;
import org.eclipse.jgit.api.SubmoduleUpdateCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.submodule.SubmoduleStatus;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.tts.consts.Global;

/**
 * 项目名称：  gitoper   
 * 类名称：  Clone   
 * 描述：  克隆仓库操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月5日 上午10:15:48 
 * 修改人：gaocheng    修改日期： 2017年6月5日
 * 修改备注：
 *
 */
public class Clone {
	
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

			//账号提供认证
			cloneCommand.setCredentialsProvider(Global.credentialsProvider);
			
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
			submoduleUpdate.setCredentialsProvider(Global.credentialsProvider);
			
			
			
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
	 * 描述：      删除文件
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
	public static String getSonRepo(String localUrl) throws IOException, GitAPIException{
		Git git = Git.open(new File(localUrl));
		String sonFile = "";
		//根据status获取对应的子仓文件夹名称
		for (java.util.Map.Entry<String, SubmoduleStatus> s:git.submoduleStatus().call().entrySet()) {
			sonFile += s.getKey() + ","; 	
		}
		return sonFile;
	}
}
