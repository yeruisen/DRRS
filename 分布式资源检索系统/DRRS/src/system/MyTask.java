package system;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.TimerTask;

import javax.servlet.ServletContext;


import distributed.IServiceImpl;
//����������ʱ��Ҫִ�е�����
public class MyTask extends TimerTask{  
	private ServletContext context = null;
	public MyTask(ServletContext context){
		this.context=context;
	}
	public MyTask(){}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		context.log("��ʼ�߳�");
		StartTypes type=new StartTypes(context);
		IndexStart ins=new IndexStart(context);
		ins.setPriority(5);   
		type.setPriority(4);
		ins.start();
		context.log("��ʼ�߳�StartTypes");
		type.start();
		context.log("��ʼ�߳�IndexStart");
		try {
			//System.setSecurityManager(new RMISecurityManager()); 
			IServiceImpl is=new IServiceImpl();
			context.log("���ڷ���Զ�̵��÷���");
			LocateRegistry.createRegistry(1234);
			Naming.rebind("rmi://localhost:1234/MyTask", is);
			context.log("����Զ�̵��÷����ɹ�");
		} catch (RemoteException e) {
			
			// TODO Auto-generated catch block
			context.log("����Զ�̵��÷��������쳣1");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			context.log("����Զ�̵��÷��������쳣2");
			e.printStackTrace();
		}
	}
	
}
