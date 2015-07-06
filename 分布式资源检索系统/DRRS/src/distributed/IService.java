package distributed;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public interface IService extends Remote{     //Զ�̵��ýӿ�
	//Զ�̵��ò�ѯ����
	Map<List<String>, Float> Isearch(String str1,String []str2,String []str3,String tag)throws RemoteException;
	//Զ�̵���ע�������IP
	ArrayList<String> registIP(String hostIP,String remoteIP)throws RemoteException;
	//Զ�̵�������¼���ķ�����IP
	void addIP(String hostIP)throws RemoteException;
	//Զ�̵����Ƴ�ע���ķ�����IP
	void deleteIP(String hostIP,String remoteIP)throws RemoteException;
	
	String isLive()throws RemoteException;
	
	/*
	 * ѡ��һ̨Ϊ��������
	 */
	void willHost()throws RemoteException;
	/*
	 * �ӻ�����remoteIP
	 */
	void updatRemoteIP(String remote)throws RemoteException;
	/*����Զ�̵��÷���
	 */
}
