package distributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class UpdateHostIP  {
	private String remoteIP;

	public UpdateHostIP(String remoteIP ) {  //�����ӻ�IP
		this.remoteIP = remoteIP;
	}

	public void run1() {
		
		IService is;
		String host;
		host = this.remoteIP + ":1234";
		try {
			
			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			//is.deleteIP(this.remoteIP,this.hostIP);
			System.out.println("��һ��ִ��is.willHost()");
			is.willHost();
			System.out.println("���³ɹ�"+this.remoteIP);
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
