package distributed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;




import search.Index;
import search.Search;
import waysThread.AddIPThread;
import waysThread.HostSearchThread;
import waysThread.IsLiveHostListener;
import waysThread.RemoveIPThread;
import waysThread.SearchThread;

public class MainIPTable { // ����������
	// public List<String> IPTable;
	public String host;
	public String remote;
	private String IPTableXMLPath = "";
	public MainIPTable() {
		IPTableXMLPath = getIPTablePath();
	}

	// ��������ʼ��IPTable
	public MainIPTable(String ho, String rem) {
		// this.IPTable=new ArrayList<String>();
		this.host = ho;
		this.remote = rem;
		IPTableXMLPath = getIPTablePath();
	}
	
	//���IPTable.xml·��
	private String getIPTablePath(){
		
		System.out.println("#########getIPTablePath###########");
		Index in = new Index(1);
		//System.out.println(in.getWebappsPath()+"IPTable.xml\n"+"############\n");
		return in.getWebappsPath()+"IPTable.xml";
		
	}
	
	//���test.xml·��
	private String getXMLPath(){
		System.out.println("#########getXMLPath#############");
		Index in = new Index(1);
		//System.out.println(in.getXmlPath()+"\n############\n");
		return in.getXmlPath();
		
	}

	// ����һ��IP��
	public void createIPTable() throws IOException {
		// this.IPTable=new ArrayList<String>();
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

	// �ж�IP���Ƿ��Ѿ����ڴ�IP
	private boolean isHave(String IP) {
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

	// ���һ��IP
	public ArrayList<String> addNewIP(String IP, String remoteIP)
			throws DocumentException {
		// System.out.println(isHave(IP));
		System.out.println("ok????????????");
		ArrayList<String> IPList = new ArrayList<String>();
		System.out.println(IP);
		if (!isHave(IP)) {
			System.out.println("�����У�������");
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
		// ����Զ�̵�addIP������������������������������
		SAXReader reader = new SAXReader();
		ArrayList<AddIPThread> add = new ArrayList<AddIPThread>();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			if (!element.getText().equals(IP)
					&& !element.getText().equals(remoteIP)) {
				System.out.println("yeruisen??????"+element.getText());
				IPList.add(element.getText());
				add.add(new AddIPThread(element.getText(), IP));
			}
		}
		Iterator<AddIPThread> it1 = add.iterator();
		while (it1.hasNext()) {
			it1.next().start();
		}
		return IPList;
	}

	// ��IP�����Ƴ�ע���ķ�����IP
	public void remove(String IP, String remoteIP) throws DocumentException {
		ArrayList<String> IPList = new ArrayList<String>();
		System.out.println("�ǲ���ִ��������");
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(this.getIPTablePath()));
			Element root = doc.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				if (element.getText().equals(IP)) {
					it.remove();
					System.out.println("�Ƴ���" + element.getText());
				}
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

		SAXReader reader = new SAXReader();
		ArrayList<RemoveIPThread> remove = new ArrayList<RemoveIPThread>();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			if (!element.getText().equals(IP)
					&& !element.getText().equals(remoteIP)) {
				IPList.add(element.getText());
				remove.add(new RemoveIPThread(IP, element.getText()));
			}
		}
		Iterator<RemoveIPThread> it1 = remove.iterator();
		while (it1.hasNext()) {
			it1.next().start();
		}
	}


	public Map<List<String>, Float> search(String str1,
			String[] str2, String[] str3,String tag) throws DocumentException {
		
		Map<List<String>, Float> finalResult = null;
		// ����һ���̳߳�
		ExecutorService exec = Executors.newCachedThreadPool();
		// Callable����˳���
		ArrayList<Callable<Map<List<String>, Float>>> results = new ArrayList<Callable<Map<List<String>, Float>>>();
		//Future ����˳���
		ArrayList<Future<Map<List<String>, Float>>> future;
		//������
		List<Map<List<String>, Float>> mapResult = new ArrayList<Map<List<String>, Float>>();
		//��ע���
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		results.add(new HostSearchThread(str1, str2, str3));
		
		Element element;
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {

			element = (Element) it.next();
			if (!element.getText().equals(this.host)) {
				System.out.println("element.getText()"+element.getText());
				results.add(new SearchThread(element.getText(),
						str1, str2, str3,tag));
			}
		}
		System.out.println("this.host="+this.host);
		System.out.println("this.remote="+this.remote);
		try {
			future = (ArrayList<Future<Map<List<String>, Float>>>) exec
					.invokeAll(results);
			for (Future<Map<List<String>, Float>> fs : future) {
				if (fs.isDone()) {
					try {
						System.out.println("ִ���˼���");
						System.out.println(fs.toString());
						mapResult.add(fs.get());
						System.out.println("����"+fs.get().size());
						System.out.println(fs.get().values());
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
				else {
					System.out.println("Future result is not yet complete");
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//�ر��̳߳�
		
		exec.shutdown();
		//���ؽ��
		try {
			finalResult=Search.sort(mapResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalResult;
	}
	
	/*�������������˳���֪ͨ��һ������������ķ�������Ϊ����������
	 * ��һ����Ҫ����Զ��ע������������ִ��ע������
	 */
	

	public void destroy () throws DocumentException{
		// ����Զ�̷���deleteIP����ɾ������ֹ��ѡ
		//ѡ�ٵ�һ̨��Ϊ��������,������ע���ע������ٽ���Զ�̷������ã����������ӻ���ע���
		//ArrayList<String> IPList = new ArrayList<String>();
		
		SAXReader reader = new SAXReader();
		ArrayList<RemoveIPThread> remove = new ArrayList<RemoveIPThread>();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		String  candidateIP = null;
		System.out.println(this.host);
		int tag=0;//��tag����1��ʱ���¼��ʱ��ip,���ұ�����candidateIP��
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			if (!element.getText().equals(this.host)) {
				tag++;
				if(tag==1){
					candidateIP = element.getText();
					System.out.println("tag="+tag);
				}
				//IPList.add(element.getText());
				remove.add(new RemoveIPThread(this.host, element.getText()));
			}
		}
		Iterator<RemoveIPThread> it1 = remove.iterator();
		while (it1.hasNext()) {
			it1.next().start();
		}
		/*
		 * �������˳�ǰ������Դ���Զ������
		 */
		if(candidateIP !=null){
		SAXReader reader1 = new SAXReader();
		Document doc1= reader1.read(new File(getXMLPath()));
		Element root = doc1.getRootElement();
		root.attribute("remote").setValue(candidateIP);
		try {
			
			java.io.OutputStream out=new java.io.FileOutputStream(new File(getXMLPath()));
			  java.io.Writer wr=new java.io.OutputStreamWriter(out,"UTF-8");  
			  doc1.write(wr);  
			  wr.close();
			  out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * ѡ�ٵ�һ̨��Ϊ��������
		 */
		UpdateHostIP up = new UpdateHostIP(candidateIP);
		System.out.println("�������е�����");
		up.run1();
		}
		else{
		}
		File file = new File(IPTableXMLPath);
		System.out.println(IPTableXMLPath+"hahahhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			if (file.exists()) {
				file.delete();
			}
	}
	public void checkDown() throws InterruptedException, ExecutionException, DocumentException{
		ExecutorService exec = Executors.newCachedThreadPool();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		//LinkedHashMap<Future<String>,String> future;
		LinkedHashMap<Future<String>,String> results = new LinkedHashMap<Future<String>,String>();
		Element element;
		System.out.println("��ʼ����ע���");
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
			element = (Element) it.next();
			System.out.println("���ڱ���ע���"+element.getText());
			if (!element.getText().equals(this.host)) {
				System.out.println("��鵱�����element.getText()"+element.getText());
				results.put(exec.submit(new IsLiveHostListener(element.getText())),element.getText());
			}
		}
		
		for (Entry<Future<String>, String> entry : results.entrySet()) {
			System.out.println("�����Ƿ�ִ����");
			System.out.println(entry.getKey());
			if (entry.getKey().get().equals("false")) {
				System.out.println("�Ƿ�����"+entry.getKey().get());
				this.remove(entry.getValue(), this.host);
			}
			else
			System.out.println("�����š�����������������������");
		}
		//�ر��̳߳�
		exec.shutdown();	
	}
	
}
