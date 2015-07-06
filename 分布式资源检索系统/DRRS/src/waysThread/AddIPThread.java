package waysThread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import distributed.IService;

public class AddIPThread extends Thread {
	private String remoteIP;
	private String hostIP;

	public AddIPThread(String remoteIP, String hostIP) {
		this.hostIP = hostIP;
		this.remoteIP = remoteIP;
	}

	@Override
	public void run() {
	
		IService is;
		String host;
		host = this.remoteIP + ":1234";
		try {
			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			is.addIP(this.hostIP);
			System.out.println("添加成功");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常");
			e.printStackTrace();
		}
	}
}
