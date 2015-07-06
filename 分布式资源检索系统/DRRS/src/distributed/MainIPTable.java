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

public class MainIPTable { // 主服务器类
	// public List<String> IPTable;
	public String host;
	public String remote;
	private String IPTableXMLPath = "";
	public MainIPTable() {
		IPTableXMLPath = getIPTablePath();
	}

	// 构造器初始化IPTable
	public MainIPTable(String ho, String rem) {
		// this.IPTable=new ArrayList<String>();
		this.host = ho;
		this.remote = rem;
		IPTableXMLPath = getIPTablePath();
	}
	
	//获得IPTable.xml路径
	private String getIPTablePath(){
		
		System.out.println("#########getIPTablePath###########");
		Index in = new Index(1);
		//System.out.println(in.getWebappsPath()+"IPTable.xml\n"+"############\n");
		return in.getWebappsPath()+"IPTable.xml";
		
	}
	
	//获得test.xml路径
	private String getXMLPath(){
		System.out.println("#########getXMLPath#############");
		Index in = new Index(1);
		//System.out.println(in.getXmlPath()+"\n############\n");
		return in.getXmlPath();
		
	}

	// 创建一个IP表
	public void createIPTable() throws IOException {
		// this.IPTable=new ArrayList<String>();
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

	// 判断IP表是否已经存在此IP
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

	// 添加一个IP
	public ArrayList<String> addNewIP(String IP, String remoteIP)
			throws DocumentException {
		// System.out.println(isHave(IP));
		System.out.println("ok????????????");
		ArrayList<String> IPList = new ArrayList<String>();
		System.out.println(IP);
		if (!isHave(IP)) {
			System.out.println("进行中？？？？");
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
		// 调用远程的addIP、、、、、、、、、、、、、、、
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

	// 从IP表中移除注销的服务器IP
	public void remove(String IP, String remoteIP) throws DocumentException {
		ArrayList<String> IPList = new ArrayList<String>();
		System.out.println("是不是执行了两次");
		try {
			SAXReader reader = new SAXReader();
			Document doc = reader.read(new File(this.getIPTablePath()));
			Element root = doc.getRootElement();
			for (Iterator<?> it = root.elementIterator(); it.hasNext();) {
				Element element = (Element) it.next();
				if (element.getText().equals(IP)) {
					it.remove();
					System.out.println("移除了" + element.getText());
				}
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
		// 创建一个线程池
		ExecutorService exec = Executors.newCachedThreadPool();
		// Callable放入顺序表
		ArrayList<Callable<Map<List<String>, Float>>> results = new ArrayList<Callable<Map<List<String>, Float>>>();
		//Future 存入顺序表
		ArrayList<Future<Map<List<String>, Float>>> future;
		//保存结果
		List<Map<List<String>, Float>> mapResult = new ArrayList<Map<List<String>, Float>>();
		//读注册表
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
						System.out.println("执行了几次");
						System.out.println(fs.toString());
						mapResult.add(fs.get());
						System.out.println("长度"+fs.get().size());
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
		//关闭线程池
		
		exec.shutdown();
		//返回结果
		try {
			finalResult=Search.sort(mapResult);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalResult;
	}
	
	/*主服务器主动退出，通知第一个加入搜索域的服务器作为主服务器，
	 * 这一步需要调用远程注销方法，并且执行注册表更新
	 */
	

	public void destroy () throws DocumentException{
		// 调用远程方法deleteIP，先删除，防止误选
		//选举第一台作为主服务器,更新其注册表，注册表里再进行远程方法调用，更新其他从机的注册表
		//ArrayList<String> IPList = new ArrayList<String>();
		
		SAXReader reader = new SAXReader();
		ArrayList<RemoveIPThread> remove = new ArrayList<RemoveIPThread>();
		Document doc = reader.read(new File(this.getIPTablePath()));
		Element el = doc.getRootElement();
		String  candidateIP = null;
		System.out.println(this.host);
		int tag=0;//当tag等于1的时候记录此时的ip,并且保存在candidateIP里
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
		 * 主机在退出前更新资源里的远程主机
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
		 * 选举第一台作为主服务器
		 */
		UpdateHostIP up = new UpdateHostIP(candidateIP);
		System.out.println("到底运行到哪了");
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
		System.out.println("开始遍历注册表");
		for (Iterator<?> it = el.elementIterator(); it.hasNext();) {
			element = (Element) it.next();
			System.out.println("正在遍历注册表"+element.getText());
			if (!element.getText().equals(this.host)) {
				System.out.println("检查当机情况element.getText()"+element.getText());
				results.put(exec.submit(new IsLiveHostListener(element.getText())),element.getText());
			}
		}
		
		for (Entry<Future<String>, String> entry : results.entrySet()) {
			System.out.println("遍历是否执行完");
			System.out.println(entry.getKey());
			if (entry.getKey().get().equals("false")) {
				System.out.println("是否检测完"+entry.getKey().get());
				this.remove(entry.getValue(), this.host);
			}
			else
			System.out.println("还活着。。。。。。。。。。。。");
		}
		//关闭线程池
		exec.shutdown();	
	}
	
}
