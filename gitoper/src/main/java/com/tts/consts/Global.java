package com.tts.consts;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Global {
	
		//git地址   
		public static String gitUrl = "https://github.com/wuyuleiliu/gitoper.git";
//		public static String gitUrl = "http://gaocheng@172.19.3.125/gaocheng/testjgit.git";
		
		//git账号
		public static String username = "792564728@qq.com";
//		private static String username = "gaocheng";
		
		//git账号密码
		private static String password = "gaocheng328";
		
		public static CredentialsProvider credentialsProvider = 
				new UsernamePasswordCredentialsProvider(username,password);
		//本地仓库地址
		public static String localUrl = "e:\\git\\gaocheng";
			
		//切换分支名
		public static String branchName = "remotes/origin/";
		
		//需要提交的文件名，多文件以逗号分隔,为空默认所有文件add
		public static String commitFile = "";
		
		//存储提交版本信息（push）
		public static Map<String,String> pushMes = new HashMap<String, String>();
		
		//commit的文件名
		public static String fileNames;
}
