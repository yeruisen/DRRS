package distributed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import search.Index;
import waysThread.UpdateOtherRemoteIP;

public class CommonTable { // 一般服务器类
	// IP表
	// public List<String> IPTable;
	public String host;
	public String remote;

	public CommonTable() {

	}

	// 构造器初始化IPTable
	public CommonTable(String ho, String rem) {
		// this.IPTable=new ArrayList<String>();
		this.host = ho;
		this.remote = rem;
	}
	
	
	//获得IPTable.xml路径
	private String getIPTablePath(){
		
		System.out.println("#########getIPTablePath##########");
		Index in = new Index(1);
		//System.out.println(in.getWebappsPath()+"IPTable.xml\n"+"############\n");
		return in.getWebappsPath()+"IPTable.xml";
		
	}
	
	//获得test.xml路径
	private String getXMLPath(){
		System.out.println("#########getXMLPath##########");
		Index in = new Index(1);
		//System.out.println(in.getXmlPath()+"\n############\n");
		return in.getXmlPath();
		
	}

	// 建立一个XML文档
	public void createIPTable() throws IOException {
		File file = new File(this.getIPTablePath());
		if (file.exists()) {
			file.createNewFile();
		}
		Document document = DocumentHelper.createDocument();// 建立document
		Element tableElement = document.addElement("IPTable");// 添加根节点
		tableElement.addAttribute("host", this.host); // 给根节点赋值
		tableElement.addAttribute("manager", this.remote); // 给根节点赋值
		Element remoteElement = tableElement.addElement("remote"); // 添加一个remote节点
		remoteElement.addText(remote);// 给remote节点添加内容
		// 将document中的内容写入文件中
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileWriter(new File(this.getIPTablePath())));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 向远程主服务器注册
	public void registToRemote(String host, String remote) {

		System.out.println("remote"+remote);
		System.out.println("开始注册");
		ArrayList<String> IPList;
		IService is;
		String Host = remote + ":1234";
		System.out.println("开始注册Host"+Host);
		try {
			is = (IService) Naming.lookup("rmi://" + Host + "/MyTask");
			System.out.println("开始注册Host1"+Host);
			System.out.println("开始注册Host1"+Host);
			System.out.println("开始注册Host1"+Host);
			System.out.println("开始注册Host1"+Host);
			IPList = is.registIP(host, remote);
			System.out.println("注册结束");
			this.loadIPTable(IPList);
			System.out.println("加载结束");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常1");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常2");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("出现异常3");
			e.printStackTrace();
		}
	}

	// 用于服务器向主服务器发送注销请求
	public void destroy(String ho, String re) {

		// 调用远程方法deleteIP
		IService is;
		String Host = re + ":1234";
		try {

			is = (IService) Naming.lookup("rmi://" + Host + "/MyTask");
			System.out.println("删了吗？？？？？");
			// 调用远程方法向主服务器请求删除
			is.deleteIP(ho, re);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File file = new File(this.getIPTablePath());
		if (file.exists()) {
			file.delete();
		}
	}

	// 把IPTable写进注册表里
	void loadIPTable(ArrayList<String> listIP) {
		Iterator<String> it1 = listIP.iterator();
		String ip;
		while (it1.hasNext()) {
			ip = it1.next();
			if (!isHave(ip)) {
				SAXReader reader = new SAXReader();
				try {
					Document doc = reader.read(new File(this.getIPTablePath()));
					Element root = doc.getRootElement();
					// Element id=root.addElement("id");
					Element remote = root.addElement("remote");
					remote.setText(ip);
					XMLWriter writer;
					writer = new XMLWriter(new FileOutputStream(this.getIPTablePath()));
					writer.write(doc);
					writer.close();

				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// 添加一个IP
	void newIP(String IP) {
		if (!isHave(IP)) {
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(new File(this.getIPTablePath()));
				Element root = doc.getRootElement();
				// Element id=root.addElement("id");
				Element remote = root.addElement("remote");
				remote.setText(IP);
				XMLWriter writer;
				writer = new XMLWriter(new FileOutputStream(this.getIPTablePath()));
				writer.write(doc);
				writer.close();

			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// //删除一个节点
	void remove(String IP) {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(this.getIPTablePath()));
			Element root = doc.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				if (element.getText().equals(IP))
					it.remove();
				// do something
			}
			// 将doc中的内容写入文件中
			try {
				FileWriter newFile = new FileWriter(new File(this.getIPTablePath()));
				XMLWriter newWriter = new XMLWriter(newFile);
				newWriter.write(doc);
				newWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 判断IP表里面是否含有某个IP
	boolean isHave(String IP) {
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(this.getIPTablePath()));
			Element el = doc.getRootElement();
			for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				if (element.getText().equals(IP)) {
					return true;
				} else {
					continue;
				}
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/*
	 * 更改注册表使自己为主机
	 */
	void update() throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element root = doc.getRootElement();
		String str = root.attributeValue("host");
		String str1 = root.attributeValue("manager");
		System.out.println("host" + str);
		System.out.println("manager" + root.attributeValue("manager"));
		
		root.attribute("manager").setValue(str);
		
		System.out.println("manager" + root.attributeValue("manager"));
		
		
		root.addElement("remote").addText(str);
		UpdateIP upd = new UpdateIP(str);
		upd.runs();
		//ArrayList<String> IPList = new ArrayList<String>();
		ArrayList<UpdateOtherRemoteIP> up = new ArrayList<UpdateOtherRemoteIP>();
		System.out.println("host="+str);
		for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			if (!element.getText().equals(str)&&!element.getText().equals(str1)) {
				System.out.println("element.getText()="+element.getText());
				//IPList.add(element.getText());
				up.add(new UpdateOtherRemoteIP(element.getText(),str));
			}
		}
		Iterator<UpdateOtherRemoteIP> it1 = up.iterator();
		while (it1.hasNext()) {
			it1.next().start();
		}
		
		
		// 将doc中的内容写入文件中
		try {
			FileWriter newFile = new FileWriter(new File(this.getIPTablePath()));
			XMLWriter newWriter = new XMLWriter(newFile);
			newWriter.write(doc);
			newWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 更新从机注册表中的主机
	 */
	void updateRemote(String remote) throws DocumentException {

		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element root = doc.getRootElement();
		//String str = root.attributeValue("host");
		//System.out.println("host" + str);
		//System.out.println("manager" + root.attributeValue("manager"));
		// root.addAttribute("manager", str);
		root.attribute("manager").setValue(remote);
		
		System.out.println(remote+"?????????????????");
		//System.out.println("manager" + root.attributeValue("manager"));
		// 将doc中的内容写入文件中
		try {
			FileWriter newFile = new FileWriter(new File(this.getIPTablePath()));
			XMLWriter newWriter = new XMLWriter(newFile);
			newWriter.write(doc);
			newWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		SAXReader reader1 = new SAXReader();
		Document doc1= reader1.read(new File(this.getXMLPath()));
		Element root1 = doc1.getRootElement();
		root1.attribute("remote").setValue(remote);
		try {
			
			java.io.OutputStream out=new java.io.FileOutputStream(new File(this.getXMLPath()));
			  java.io.Writer wr=new java.io.OutputStreamWriter(out,"UTF-8");  
			  doc1.write(wr);  
			  wr.close();
			  out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
