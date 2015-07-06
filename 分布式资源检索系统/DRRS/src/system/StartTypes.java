package system;
import javax.servlet.ServletContext;

import distributed.CommonTable;
import distributed.MainIPTable;
import search.Index;
public class StartTypes extends Thread{	
	private static boolean isRunning = false;   
	private ServletContext context = null; 
	//private Index index=new Index();
//	public String host;
//	public String remote;
     public StartTypes(ServletContext context){    
         this.context = context ;  
     } 
	@Override
	public void run(){        //实现服务器启动时的线程
		// TODO Auto-generated method stub
		String host;
		String remote;
		//Index index;
		try{
			//index = new Index();
			host=Index.getHost();
			System.out.println(host);
			remote=Index.getRemote();
			System.out.println(remote);
			//index.indexDestory();
			if (! isRunning){  
				isRunning = true ;
				context.log("开始任务读取文件.") ; 
				if(host.equals(remote)){ //hostIP与remoteIP相等则为主服务器
					System.out.println("第一");
					MainIPTable main=new MainIPTable(host,remote);
					main.createIPTable();
					//main.addNewIP(host);
				}
				else{                   //hostIP与remoteIP不相等则为一般服务器
					CommonTable com=new CommonTable(host,remote);
					System.out.println("第二");
					com.createIPTable();
					com.registToRemote(host, remote);   //向主服务器注册
				}
			}
		}catch(Exception e){}
	}

}
