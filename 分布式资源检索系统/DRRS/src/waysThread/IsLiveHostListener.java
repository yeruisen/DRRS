package waysThread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import distributed.IService;

//主机判断是否有机器当机

public class IsLiveHostListener  implements Callable<String>{
	String remote;
	public IsLiveHostListener(String remote){
		this.remote=remote;
	}
	public String way(){
		String str = null;
		IService is;
		String host;
		host = this.remote + ":1234";
		try {

			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			str=is.isLive();
			System.out.println(str);
			System.out.println("还活着"+this.remote);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常1");
			str = "false";
			return str;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常2");
			str = "false";
			return str;
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常3");
			str = "false";
			return str;
		}
		return str;
	}
	public String call() {
		// TODO Auto-generated method stub
		return this.way();
	}
}
