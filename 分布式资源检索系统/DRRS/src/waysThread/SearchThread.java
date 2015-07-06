package waysThread;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import distributed.IService;

public class SearchThread implements Callable<Map<List<String>, Float>> {
	private String remoteIP;
	private String str1;
	private String[] str2;
	private String[] str3;
	private String tag;
	private Map<List<String>, Float> IPMap;

	public SearchThread(String remoteIP,  String str1,
			String[] str2, String[] str3,String tag) {
		this.remoteIP = remoteIP;
		this.str1 = str1;
		this.str2 = str2;
		this.str3 = str3;
		this.tag = tag;
		this.IPMap = new LinkedHashMap<List<String>, Float>();
	}

	public LinkedHashMap<List<String>, Float> ipWays() {

		
		// 查询远程主机的资源

		IService is;
		String host;
		host = this.remoteIP + ":1234";
		try {
			
			is = (IService) Naming.lookup("rmi://" + host + "/MyTask");
			this.IPMap.putAll(is.Isearch(this.str1, this.str2, this.str3,this.tag));
			System.out.println("查询"+this.remoteIP);
			System.out.println("查询成功");
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
		return (LinkedHashMap<List<String>, Float>) this.IPMap;
	}

	@Override
	public Map<List<String>, Float> call() throws Exception {
		// TODO Auto-generated method stub
		return ipWays();
	}
}
