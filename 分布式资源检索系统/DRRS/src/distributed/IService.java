package distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface IService extends Remote{     //远程调用接口
	//远程调用查询方法
	Map<List<String>, Float> Isearch(String str1,String []str2,String []str3,String tag)throws RemoteException;
	//远程调用注册服务器IP
	ArrayList<String> registIP(String hostIP,String remoteIP)throws RemoteException;
	//远程调用添加新加入的服务器IP
	void addIP(String hostIP)throws RemoteException;
	//远程调用移除注销的服务器IP
	void deleteIP(String hostIP,String remoteIP)throws RemoteException;
	
	String isLive()throws RemoteException;
	
	/*
	 * 选举一台为主服务器
	 */
	void willHost()throws RemoteException;
	/*
	 * 从机更新remoteIP
	 */
	void updatRemoteIP(String remote)throws RemoteException;
	/*其他远程调用方法
	 */
}
