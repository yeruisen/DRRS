package waysThread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import distributed.IService;

public class UpdateOtherRemoteIP extends Thread {
	private String remoteIP;
	private String hostIP;
	public UpdateOtherRemoteIP(String remoteIP ,String hostIP//��Ϊ������ip
			) {  //�����ӻ�IP
		this.remoteIP = remoteIP;
		this.hostIP = hostIP;
	}

	@Override
	public void run() {
		
		IService is;
		String host;
		host = this.remoteIP + ":1234";
		try {
			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			//is.deleteIP(this.remoteIP,this.hostIP);
			is.updatRemoteIP(this.hostIP);
			System.out.println("���³ɹ�this.remoteIP="+this.remoteIP);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣");
			e.printStackTrace();
		}
	}
}
