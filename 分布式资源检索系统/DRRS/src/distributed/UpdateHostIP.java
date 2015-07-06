package distributed;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class UpdateHostIP  {
	private String remoteIP;

	public UpdateHostIP(String remoteIP ) {  //其他从机IP
		this.remoteIP = remoteIP;
	}

	public void run1() {
		
		IService is;
		String host;
		host = this.remoteIP + ":1234";
		try {
			
			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			//is.deleteIP(this.remoteIP,this.hostIP);
			System.out.println("下一步执行is.willHost()");
			is.willHost();
			System.out.println("更新成功"+this.remoteIP);
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
