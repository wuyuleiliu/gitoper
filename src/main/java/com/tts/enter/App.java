package com.tts.enter;

import java.util.Scanner;

import com.tts.consts.Global;
import com.tts.opergit.Branch;
import com.tts.opergit.Clone;
import com.tts.opergit.Commit;
import com.tts.opergit.Log;
import com.tts.opergit.Pull;
import com.tts.opergit.Push;
import com.tts.opergit.Remove;
import com.tts.opergit.Reset;
import com.tts.opergit.Revert;

/**
 * 项目名称：  gitoper   
 * 类名称：  App   
 * 描述：       利用jgit操作实现对git的基本操作
 * @author  gaocheng   
 * 创建时间：  2017年5月31日 下午1:15:27 
 * 修改人：gaocheng    修改日期： 2017年5月31日
 * 修改备注：
 *
 */
public class App {

	
	
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
		System.out.println("4.push操作");
		System.out.println("5.remove操作");
		System.out.println("6.回退到某个版本代码（revert）");
		System.out.println("7.回退到某个版本代码（reset）");
		System.out.println("8.查看仓库分支信息");
		System.out.println("9.查看提交版本纪录");
		System.out.println("10.切换分支");		
		System.out.println("11.合并分支");		
		System.out.println("12.退出");
				
		Scanner s = new Scanner(System.in);
		int i = s.nextInt();
		
		try {
			switch (i) {
				case 1:
					//下载仓库（主仓和子仓）,默认为master
					Clone.cloneRepository(Global.gitUrl, Global.localUrl, "");
					Clone.cloneSubRepository(Global.localUrl);
					break;
				case 2:
					//pull操作（主仓和子仓）		
					Pull.pullRepository(Global.localUrl);
					if(Clone.getSonRepo(Global.localUrl) != null && !"".equals(Clone.getSonRepo(Global.localUrl))) {
						Pull.pullSubRepository(Global.localUrl);
					}
					System.out.println("pull完成");
					break;
				case 3:
					//commit操作（主仓+子仓） 注意  子仓库只有是自己的才能进行远程仓库更新操作
					System.out.println("请输入此次提交的信息（区分版本）");
					String mes = s.next();
					Commit.commitRepository(Global.localUrl, Global.fileNames, mes);

					if(Clone.getSonRepo(Global.localUrl) != null && !"".equals(Clone.getSonRepo(Global.localUrl))) {
						Commit.commitSubRepository(Global.localUrl, "", mes);
					}

					break;
				case 4:
					//push操作
					Push.pushRepository(Global.localUrl);
					if(Clone.getSonRepo(Global.localUrl) != null && !"".equals(Clone.getSonRepo(Global.localUrl))) {
						Push.pushSubRepository(Global.localUrl);

					}
					break;
				case 5:
					//删除操作
					System.out.println("请输入删除的文件（文件需写明后缀,多文件以逗号分隔）:");
					String file = s.next();
					Remove.removeRepository(Global.localUrl, file);
					break;
				case 6:
					//恢复代码版本
					System.out.println("请输入需要恢复的版本号:");
					
					String ver1 = s.next();
					if(!"".equals(ver1) && ver1 != null) {
						Revert.rollBackRevertCode(ver1, Global.localUrl, Log.log(Global.localUrl, false));
					}
					break;
				case 7:
					//恢复代码版本
					System.out.println("请输入需要恢复的版本号:");
					
					String ver2 = s.next();
					if(!"".equals(ver2) && ver2 != null) {
						Reset.rollBackResetCode(ver2, Global.localUrl, Log.log(Global.localUrl, false));
					}
					break;
				case 8:
					//得到远程仓库的分支
					System.out.println("当前分支为："+Branch.getCurrentBranch(Global.localUrl));
					Branch.getLocalBranchNames(Global.localUrl);
					Branch.getRemoteBranchNames(Global.localUrl, "");
					break;
				case 9:
					//查看提交版本信息

					Log.log(Global.localUrl, true);
					break;
				case 10:
					//切换分支
					System.out.println("请输入想要切换的分支（如master）:");
					String br = s.next();
					Branch.switchBranch(Global.localUrl,Global.branchName+br);
					break;
				case 11:
					//合并当前分支
					System.out.println("请输入想要合并分支的分支名（类似master）:");
					String brn = s.next();
					System.out.println("请输入想要合并当前分支的信息:");
					String message = s.next();
					Branch.mergeBranch(Global.localUrl, message, brn);
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

}
