package distributed;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.dom4j.DocumentException;


import action.Control;

import search.Index;
import search.Search;


public class IServiceImpl extends UnicastRemoteObject implements IService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IServiceImpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	// 远程调用搜索方法//两种类型、、、、、、、、、的搜索
	@Override
	public Map<List<String>,Float> Isearch(String str1, String[] str2,
			String[] str3,String tag) throws RemoteException{
		// TODO Auto-generated method stub

		Map<List<String>, Float> result = null;
		Search sea;
		try {
			sea = new Search();
			if (str1 != null && tag.equals("1")) {
				result = sea.search(str1, str2); // 普通搜索
			} 
			else if(str1 != null && tag.equals("2")){//全文检索
				result = sea.fullTextSearch(str1,str2);
				 
			}
			else if(str1 == null){
				// 精确搜索
				result = sea.preciseSearch(str2, str3);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	// 远程调用注册IP方法
	@Override
	public ArrayList<String> registIP(String hostIP, String remoteIP)
			throws RemoteException {
		{
			// TODO Auto-generated method stub
			System.out.println("hostIP="+hostIP+"remoteIP="+remoteIP);
			ArrayList<String> list = new ArrayList<String>();
			MainIPTable main = new MainIPTable();
			try {
				list = main.addNewIP(hostIP, remoteIP);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}
	}

	// 远程调用添加IP方法
	@Override
	public void addIP(String IP) throws RemoteException {
		CommonTable com = new CommonTable();
		com.newIP(IP);
	}

	// 远程调用删除IP方法
	@Override
	public void deleteIP(String hostIP, String remoteIP) throws RemoteException {
		// TODO Auto-generated method stub

		try {
			String host;
			String remote;
			//Index in = new Index();

			host = Index.getHost();
			remote = Index.getRemote();
			if (remote.equals(remoteIP)) {
				MainIPTable main = new MainIPTable();
				try {
					main.remove(hostIP, remoteIP);
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				CommonTable co = new CommonTable(host, remoteIP);
				co.remove(hostIP);
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void willHost() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("下一步执行co.update();");
		CommonTable co = new CommonTable();
try {
			
			co.update();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Control con = new Control();
		try {
			con.init();
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

	@Override
	public void updatRemoteIP(String remote) throws RemoteException{
		// TODO Auto-generated method stub
		CommonTable co = new CommonTable();
		try {
			co.updateRemote(remote);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String isLive() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("你没有调用吗?");
		return "true";
	}

}
