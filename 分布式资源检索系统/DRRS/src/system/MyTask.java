package system;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.TimerTask;

import javax.servlet.ServletContext;


import distributed.IServiceImpl;
//服务器启动时需要执行的任务
public class MyTask extends TimerTask{  
	private ServletContext context = null;
	public MyTask(ServletContext context){
		this.context=context;
	}
	public MyTask(){}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		context.log("开始线程");
		StartTypes type=new StartTypes(context);
		IndexStart ins=new IndexStart(context);
		ins.setPriority(5);   
		type.setPriority(4);
		ins.start();
		context.log("开始线程StartTypes");
		type.start();
		context.log("开始线程IndexStart");
		try {
			//System.setSecurityManager(new RMISecurityManager()); 
			IServiceImpl is=new IServiceImpl();
			context.log("正在发布远程调用方法");
			LocateRegistry.createRegistry(1234);
			Naming.rebind("rmi://localhost:1234/MyTask", is);
			context.log("发布远程调用方法成功");
		} catch (RemoteException e) {
			
			// TODO Auto-generated catch block
			context.log("发布远程调用方法出现异常1");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			context.log("发布远程调用方法出现异常2");
			e.printStackTrace();
		}
	}
	
}
