package waysThread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.Callable;

import distributed.IService;

//�����ж��Ƿ��л�������

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
			System.out.println("������"+this.remote);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣1");
			str = "false";
			return str;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣2");
			str = "false";
			return str;
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣3");
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
