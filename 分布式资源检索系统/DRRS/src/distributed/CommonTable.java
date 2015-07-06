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

public class CommonTable { // һ���������
	// IP��
	// public List<String> IPTable;
	public String host;
	public String remote;

	public CommonTable() {

	}

	// ��������ʼ��IPTable
	public CommonTable(String ho, String rem) {
		// this.IPTable=new ArrayList<String>();
		this.host = ho;
		this.remote = rem;
	}
	
	
	//���IPTable.xml·��
	private String getIPTablePath(){
		
		System.out.println("#########getIPTablePath##########");
		Index in = new Index(1);
		//System.out.println(in.getWebappsPath()+"IPTable.xml\n"+"############\n");
		return in.getWebappsPath()+"IPTable.xml";
		
	}
	
	//���test.xml·��
	private String getXMLPath(){
		System.out.println("#########getXMLPath##########");
		Index in = new Index(1);
		//System.out.println(in.getXmlPath()+"\n############\n");
		return in.getXmlPath();
		
	}

	// ����һ��XML�ĵ�
	public void createIPTable() throws IOException {
		File file = new File(this.getIPTablePath());
		if (file.exists()) {
			file.createNewFile();
		}
		Document document = DocumentHelper.createDocument();// ����document
		Element tableElement = document.addElement("IPTable");// ��Ӹ��ڵ�
		tableElement.addAttribute("host", this.host); // �����ڵ㸳ֵ
		tableElement.addAttribute("manager", this.remote); // �����ڵ㸳ֵ
		Element remoteElement = tableElement.addElement("remote"); // ���һ��remote�ڵ�
		remoteElement.addText(remote);// ��remote�ڵ��������
		// ��document�е�����д���ļ���
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

	// ��Զ����������ע��
	public void registToRemote(String host, String remote) {

		System.out.println("remote"+remote);
		System.out.println("��ʼע��");
		ArrayList<String> IPList;
		IService is;
		String Host = remote + ":1234";
		System.out.println("��ʼע��Host"+Host);
		try {
			is = (IService) Naming.lookup("rmi://" + Host + "/MyTask");
			System.out.println("��ʼע��Host1"+Host);
			System.out.println("��ʼע��Host1"+Host);
			System.out.println("��ʼע��Host1"+Host);
			System.out.println("��ʼע��Host1"+Host);
			IPList = is.registIP(host, remote);
			System.out.println("ע�����");
			this.loadIPTable(IPList);
			System.out.println("���ؽ���");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣1");
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣2");
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			System.out.println("�����쳣3");
			e.printStackTrace();
		}
	}

	// ���ڷ�������������������ע������
	public void destroy(String ho, String re) {

		// ����Զ�̷���deleteIP
		IService is;
		String Host = re + ":1234";
		try {

			is = (IService) Naming.lookup("rmi://" + Host + "/MyTask");
			System.out.println("ɾ���𣿣�������");
			// ����Զ�̷�����������������ɾ��
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

	// ��IPTableд��ע�����
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

	// ���һ��IP
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

	// //ɾ��һ���ڵ�
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
			// ��doc�е�����д���ļ���
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

	// �ж�IP�������Ƿ���ĳ��IP
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
	 * ����ע���ʹ�Լ�Ϊ����
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
		
		
		// ��doc�е�����д���ļ���
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
	 * ���´ӻ�ע����е�����
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
		// ��doc�е�����д���ļ���
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
