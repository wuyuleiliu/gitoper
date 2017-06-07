package com.tts.opergit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * 项目名称：  jgitoper   
 * 类名称：      Init   
 * 描述：     init操作类
 * @author  gaocheng   
 * 创建时间：  2017年6月6日 下午5:07:24 
 * 修改人：gaocheng    修改日期： 2017年6月6日
 * 修改备注：
 *
 */
public class Init {
	
	/** 
	 * 方法名：  initRepo 
	 * 描述：       git init操作
	 * @author  gaocheng   
	 * 创建时间：2017年6月6日 下午5:08:33
	 *
	 */
	public static void initRepo(){
		
		InitCommand initCommand = Git.init();
		
		try {
			initCommand.setBare(false);
			initCommand.setDirectory(new File("E:\\testgit"));
			
			initCommand.call();
			

			Git git = Git.open(new File("E:\\testgit"));
			
			RemoteAddCommand remoteAddCommand = git.remoteAdd();
			
			// git remote add origin https://github.com/zhchnchn/HelloWorld.git
			remoteAddCommand.setUri(new URIish("https://github.com/wuyuleiliu/TestInit.git"));
			remoteAddCommand.setName("origin");


			remoteAddCommand.call();
			
			PushCommand pushCommand = git.push();
			
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("792564728@qq.com",
					"gaocheng328");
			pushCommand.setCredentialsProvider(credentialsProvider);
			
			pushCommand.call();
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
}
