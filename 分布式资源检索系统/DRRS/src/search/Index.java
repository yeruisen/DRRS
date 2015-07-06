package search;

import search.SAXXML;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
//import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.Word6Extractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.wltea.analyzer.lucene.IKAnalyzer;

import search.Search;
//import org.apache.tika.metadata.Metadata;
//import org.apache.tika.parser.AutoDetectParser;
//import org.apache.tika.parser.ParseContext;
//import org.apache.tika.parser.Parser;
//import org.apache.tika.sax.BodyContentHandler;
//import org.xml.sax.ContentHandler;

//import com.google.gson.FieldNamingPolicy;

//import java.io.*;
//import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

public class Index {
	
	/*
	 * ������ַλ��web����Ŀ¼�µ�index�ļ�����,�������ĸ��ļ���,�ֱ��Ӧ�ĸ�����
	 * ͬ��ʴʵ�λ��web����Ŀ¼�µ�dic�ļ�����,������ͬ��ʴ��ּ�ת���������ɵ��м��ļ�
	 * xmlλ��webapps�ļ�����
	 * ��Դ�ļ�λ��wepapps�ļ����µ�resources�ļ�����
	 */
	
	private IndexWriter writerX;//xml������
	private IndexWriter writerPX;//xml��ȷ����������
	private IndexWriter writerF;//ȫ�ļ���������
	private IndexWriter writerSyn;//�������������
	private SAXXML sxml;//xml������
	
	private static String indexXmlDir = "";//xml��ͨ����������ַ
	private static String indexPreXmlDir = "";//xml��ȷ����������ַ
	private static String indexFullDir = "";//ȫ�ļ���������ַ
	private static String dicdir = "";//ͬ��ʴ���
	private static String dmcdir = "";//dmc
	private static String synIndexDir = "";//ͬ�������λ��
	
	//private static final String PROJECT_NAME = "DRRS";	//������
	private static String webapps = "";	//...webapps\...
	
	private static String xmlDir = "";//xml��ַ
	
	private List<Document> docs[];//xml��������ֵ

	
	//����·��
	private void setPath() throws UnsupportedEncodingException{
		
		//��������·���ȵ�.....
		String path = URLDecoder.decode(this.getClass().getResource("/").getFile(), "UTF-8");
		path = path.substring(1);
		System.out.println("path:"+path);
		String paths[] = path.split("/");
		int length = paths.length-3;
		
		path = "";
		
		for(int i =0; i < length; i++)
			path = path + paths[i] + "\\";
		
		webapps = path;
		
		indexXmlDir = path + paths[length] + "\\index\\Index_xml";
		indexPreXmlDir = path + paths[length] + "\\index\\Index_xml_pre"; 
		indexFullDir = path + paths[length] + "\\index\\Index_full";
		synIndexDir = path + paths[length] + "\\index\\Index_syn";
		dicdir = path + paths[length] + "\\dic\\syn.txt";
		dmcdir = path + paths[length] + "\\dic\\dmc.txt";
		xmlDir = path + "test.xml";
		//xmlDir = "D:\\Source\\test.xml";
		
		//����Search�е�·��
		Search.SetINDEX_DIR(indexXmlDir);
		Search.SetINDEX_PREDIR(indexPreXmlDir);
		Search.SetINDEX_FULLDIR(indexFullDir);
		Search.SetINDEX_SYNDIR(synIndexDir);
		
	}
	
	//���test.xml·��
	public String getXmlPath(){
		
		return xmlDir;
		
	}
	
	//���...webapps/ 
	public String getWebappsPath(){
		
		return webapps;
		
	}
	 
	//���캯��
	public Index(int a){
		
		System.out.println("\n############Index(int)##############");
		
		try{
			
			setPath();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public Index() {
		
		System.out.println("\n*******************Index**************************\n");
		
		try{
			setPath();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Ԥ����xml
		sxml=new SAXXML();
		saxXml();
		
	}
	
	//�ӿڣ���������
	public void index() throws Exception {
		
		System.out.println("\n#########################index��ʼ############################\n");
		
		//��ͨ����������
		Directory dirX=FSDirectory.open(new File(indexXmlDir));
		IndexWriterConfig iwcX = new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
		iwcX.setOpenMode(OpenMode.CREATE);
		IndexWriter.unlock(dirX);
		writerX=new IndexWriter(dirX,iwcX);
	
		//��ȷ����������
		Directory dirPX=FSDirectory.open(new File(indexPreXmlDir));
		IndexWriterConfig iwcPX=new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
		iwcPX.setOpenMode(OpenMode.CREATE);
		IndexWriter.unlock(dirPX);
		writerPX=new IndexWriter(dirPX,iwcPX);
		
		//ȫ�ļ���������
		Directory dirF=FSDirectory.open(new File(indexFullDir));
		IndexWriterConfig iwcF = new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
		iwcF.setOpenMode(OpenMode.CREATE);
		IndexWriter.unlock(dirF);
		writerF=new IndexWriter(dirF,iwcF);
		
		//�������������
		Directory dirSyn = FSDirectory.open(new File(synIndexDir));
		IndexWriterConfig iwcSyn = new IndexWriterConfig(Version.LUCENE_36, new IKAnalyzer());
		iwcSyn.setOpenMode(OpenMode.CREATE);
		IndexWriter.unlock(dirSyn);
		writerSyn = new IndexWriter(dirSyn, iwcSyn);
		
		/***************************************************************
		//�����ĵ�����//
		Directory dirD = FSDirectory.open(new File(indexDataDir));
		IndexWriterConfig iwcD = new IndexWriterConfig(Version.LUCENE_36,new IKAnalyzer());
		iwcD.setOpenMode(OpenMode.CREATE);
		writerD = new IndexWriter(dirD, iwcD);
		***************************************************************/
		
		
		//�ȶ�xml���� (������ͨ��������ȷ����)
		int i=docs[0].size();
		for(int j=0;j<i;j++){
			
			writerX.addDocument(docs[0].get(j));
			writerPX.addDocument(docs[1].get(j));
			
		}
		writerX.forceMerge(1);
		writerPX.forceMerge(1);
		
		
		//�����������
		synIndex();
		writerSyn.forceMerge(1);
		
		
		//ȫ�ļ�������
		fullTextIndex();
		writerF.forceMerge(1);
		
		

		try{
			
			writerX.close();
			writerPX.close();
			writerF.close();
			writerSyn.close();
			
		}
		catch(Exception e){
			
			IndexWriter.unlock(dirX);
			IndexWriter.unlock(dirPX);
			IndexWriter.unlock(dirF);
			IndexWriter.unlock(dirSyn);
			
		}

		/*****************************************************
		//����Դ�ļ�������
		Document d; 
		File file = new File(dataDir);
		File[] files = file.listFiles();
		for (File f : files) {
			//txt�ļ�ֱ������
			if(!f.isDirectory()&&!f.isHidden()&&f.exists()&&f.canRead()){
				if(isTxt(f)){
					writerD.addDocument(txtIntoDoc(f));
				}
				//��txt�ļ��Ƚ��������������ļ��к���
				else {
					System.out.println(f.getName());//*******test
					d=getDocument(f);
					if(d!=null)
						writerD.addDocument(d);
				}
			}
		}
		writerD.close();
	   *******************************************************/
		
		System.out.println("\n#########################index����############################\n");
		
	}
	
	
	/**************ȫ�ļ�������*****************/
	
	private void fullTextIndex()throws Exception{
		
		System.out.println("\n#######################fullTextIndex��ʼ##########################\n");
		
		String path = webapps + "resources\\";
		
		List<Document> list = docs[2];		
		for(Document doc : list){
						
			String fpath = path+doc.get("filename");
			doc.removeField("filename");
			
			//System.out.println(fpath);
			
			String kind = doc.get("kind").trim().toLowerCase();
			
			if(kind.equals("txt")||kind.equals("doc")||kind.equals("docx")||kind.equals("ppt")
					||kind.equals("pptx")||kind.equals("xls")||kind.equals("xlsx")||kind.equals("pdf")){
				
				String text = "";
			
				if(kind.equals("txt")){
					
					//System.out.println("\n%%%%%%%%%%%%%%%%TXT%%%%%%%%%%%%%\n");
				
					doc.add(new Field("content", new FileReader(fpath)));
					writerF.addDocument(doc);
					continue;
					
				}
				else if(kind.equals("doc")){
					
					text = parseDoc(fpath);
					
				}
				else if(kind.equals("docx")){
					
					text = parseDocx(fpath);
					
				}
				else if(kind.equals("ppt")){
					
					text = parsePpt(fpath);
					
				}
				else if(kind.equals("pptx")){
					
					text = parsePptx(fpath);
					
				}
				else if(kind.equals("xls")){
					
					text = parseXls(fpath);
					
				}
				else if(kind.equals("xlsx")){
					
					text = parseXlsx(fpath);
					
				}
				else if(kind.equals("pdf")){
					
					text = parsePdf(fpath);
					
				}
				
				//System.out.println("\n^^^^^^^^^^^"+kind+"^^^^^^^^^^^^\n");
				doc.add(new Field("content", text, Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
				writerF.addDocument(doc);
	
			}
		}
		
		System.out.println("\n#######################fullTextIndex����##########################\n");
		
	}
	
	
	//����doc�ĵ�   ***
	private String parseDoc( String fpath ){
		
		String text = "";
		
		File f = new File(fpath);
		
		//System.out.println(f.getName()+":");
		
		try{
			
			InputStream is = new FileInputStream(f);
			
			WordExtractor parse = new WordExtractor(is);
			
			text = parse.getText();
			
			parse.close();
			
		}
		catch(org.apache.poi.hwpf.OldWordFileFormatException e){
			
			try{
				
				InputStream is = new FileInputStream(f);
				//Word6Extractor parse = new Word6Extractor(new HWPFOldDocument(new POIFSFileSystem(is)));
				Word6Extractor parse = new Word6Extractor(new POIFSFileSystem(is));
				text = parse.getText();
				parse.close();
				
			}
			catch(Exception e1){
				
				//e1.printStackTrace();
				
			}
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	
	//����docx�ĵ�  ***
	private String parseDocx(String fpath){
		
		String text = "";
		
		try{
			
			XWPFWordExtractor parse = new XWPFWordExtractor(POIXMLDocument.openPackage(fpath));
			
			text = parse.getText();
			
			parse.close();
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	
	//����xls�ĵ� ***
	private String parseXls(String f){
		
		String text = "";
		
		try{
			
			InputStream is = new FileInputStream(f);
			HSSFWorkbook hssw = new HSSFWorkbook(new POIFSFileSystem(is));
			ExcelExtractor parse = new ExcelExtractor(hssw);
			text = parse.getText();
			
			parse.close();
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	
	//����xlsx�ĵ� *
	private String parseXlsx(String f){
		
		String text = "";
		
		try{
			
			XSSFExcelExtractor parse = new XSSFExcelExtractor(POIXMLDocument.openPackage(f));
			text = parse.getText();
			
			parse.close();
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	//����ppt�ĵ� ***
	private String parsePpt(String f){
		
		String text = "";
		
		try{
			
			InputStream is = new FileInputStream(f);
			PowerPointExtractor parse = new PowerPointExtractor(is);
			text = parse.getText();
			
			parse.close();
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		
		
		return text;
		
	}
	
	//����pptx�ĵ� ***
	private String parsePptx(String f){
		
		String text = "";
		try{
			
			XSLFPowerPointExtractor parse = new XSLFPowerPointExtractor(POIXMLDocument.openPackage(f));
			text = parse.getText();
			
			parse.close();
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	
	//����pdf�ĵ�  ***
	private String parsePdf(String f){
		
		String text = "";
		
		try{
			
			PDFParser parser = new PDFParser(new FileInputStream(f));
			parser.parse();
			PDDocument doc = parser.getPDDocument();
			
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(doc);
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
		return text;
		
	}
	
	/*************************************/
	
	
	
	//����XML
	private void saxXml(){
		
		try{
			
			docs=sxml.getDocuments(xmlDir);
			
		}
		catch(Exception e){
			
			//e.printStackTrace();
			
		}
		
	}
	
//	//ɸѡtxt 
//	@SuppressWarnings("unused")
//	private boolean isTxt(File f){ 
//		
//		  return f.getName().toLowerCase().endsWith(".txt"); 
//	}
//	
//	//txtװdoc,����doc��������txt
//	@SuppressWarnings("unused")
//	private Document txtIntoDoc(File f) throws Exception{ 
//		
//	  		Document doc=new Document(); 
//	  		
//	  		doc.add(new Field("filename",f.getName(),Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
//	  		doc.add(new Field("fullpath",f.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS)); 
//	  		doc.add(new Field("contents",new FileReader(f))); 
//	  		
//	  		return doc;
//
//	}
	
//	//��txt��xml�ļ�����doc,������txt��xml�ļ�
//	@SuppressWarnings("unused")
//	private Document getDocument(File f) throws Exception {
//		
//		// ������Ҫ������Ԫ������
//		Set<String> tMF = new HashSet<String>();
//		tMF.add("title");
//		tMF.add("Author");
//		tMF.add("comment");
//		tMF.add("Keywords");
//		tMF.add("description");
//		tMF.add("subject");
//
//		// �����ĵ�
//		Document doc = new Document();
//
//		// �����ļ�
//		Metadata metadata = new Metadata();
//		metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
//		
//		InputStream is = new FileInputStream(f);
//		
//		Parser parser = new AutoDetectParser();
//		
//		ContentHandler handler = new BodyContentHandler();
//		
//		ParseContext context = new ParseContext();
//		context.set(Parser.class, parser);
//		
//		try{
//			
//			parser.parse(is, handler, metadata, context);
//			
//		} 
//		catch(Exception e) {
//			
//			is.close();
//			
//			System.out.println("index false"+'\n');
//			
//	  		doc.add(new Field("filename",f.getName(),Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
//	  		doc.add(new Field("fullpath",f.getCanonicalPath(),Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
//	  		
//			return doc;
//			
//		}
//
//		// ***Ϊdoc�����***//
//		
//		doc.add(new Field("filename",f.getName(),Field.Store.YES,Field.Index.ANALYZED_NO_NORMS));
//		doc.add(new Field("fullpath", f.getCanonicalPath(), Field.Store.YES,Field.Index.NOT_ANALYZED_NO_NORMS));
//		doc.add(new Field("contents", handler.toString(), Field.Store.NO,Field.Index.ANALYZED_NO_NORMS));
//		
//		//��Ԫ������ӽ�doc
//		for (String name : metadata.names()) {
//			
//			String value = metadata.get(name);
//			
//			if (tMF.contains(name)) {
//				
//				doc.add(new Field("contents", value, Field.Store.NO,
//						Field.Index.ANALYZED_NO_NORMS));
//				
//			}
//			
//			doc.add(new Field(name, value, Field.Store.YES, Field.Index.NO));
//			
//		}
//
//		return doc;
//		
//	}

	//���hostIP
//	public String getHost(){
//		
//		return docs[0].get(0).get("host");
//		
//	}
	
	@SuppressWarnings("static-access")
	public static String getHost(){
		String host = "";
		try{
			Index idx = new Index(1);
			SAXReader reader = new SAXReader();
			org.dom4j.Document document = reader.read(new File(idx.webapps+"test.xml"));
			Element element = document.getRootElement();
			Attribute attr = element.attribute("host");
			host = attr.getText();
			System.out.println("\n################################\n"+"host:"+host+"\n#################################\n");
		}
		catch(Exception e){
			e.printStackTrace();
			
			Index index = new Index();
			return index.docs[0].get(0).get("host");
			
		}
		return host;
		
	}
	
	//���remoteIP
//	public String getRemote(){
//		
//		return docs[0].get(0).get("remote");
//		
//	}
	@SuppressWarnings("static-access")
	public static String getRemote(){
		
		String remote = "";
		try{
			
			Index idx = new Index(1);
			SAXReader reader = new SAXReader();
			org.dom4j.Document document = reader.read(new File(idx.webapps+"test.xml"));
			Element element = document.getRootElement();
			Attribute attr = element.attribute("remote");
			remote = attr.getText();
			
			System.out.println("\n################################\n"+"remote:"+remote+"\n#################################\n");
		}
		catch(Exception e){
			
			e.printStackTrace();
			
			Index index = new Index();
			return index.docs[0].get(0).get("remote");
			
		}
		return remote;
		
		
	}
	
	/*********�����������**********/
	//��syn������
	private void synIndex(){
		
		//��syn.txt(ͬ��ʴ���)תdmc.txt(��ȡ��źʹ���)
		dicToDmc(dicdir, dmcdir);
		
		//��ȡdoc list
		List<Document> list = getDicDoc(dmcdir);
		
		//��������
		try{
	
			int i = list.size();
			for(int j = 0; j < i; j++)
				writerSyn.addDocument(list.get(j));
				
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		
	}
	
	
	//���syn�ĵ�list
	private List<Document> getDicDoc(String dmcdir){
		
		LinkedList<Document> doclist = new LinkedList<Document>();
		
		Map<String, List<String>> numTwords = new TreeMap<String, List<String>>();
		Map<String, List<String>> wordTnums = new TreeMap<String, List<String>>();
		
		try{
			
			BufferedReader reader = new BufferedReader( new FileReader(dmcdir) );
			String line = reader.readLine();
			
			while(line != null){
				
				String num = line.substring(0, 7);
				String word = line.substring(8);
				//System.out.println(num);
				
				LinkedList<String> list = (LinkedList<String>)numTwords.get(num); 
				
				if(list == null){
					 
					list = new LinkedList<String>();
					list.add(word);
					numTwords.put(num, list);
				 
				}
				else
					list.add(word);
				
				list = (LinkedList<String>)wordTnums.get(word);
				
				if(list == null){
					
					list = new LinkedList<String>();
					list.add(num);
					wordTnums.put(word, list);
					
				}
				else
					list.add(num);
				
				line = reader.readLine();
				
			}
			
		reader.close();
		
		for(Map.Entry<String, List<String>> entry : wordTnums.entrySet()){
			
			Document doc = new Document();
			String word = entry.getKey();
			
			doc.add(new Field("word", word, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
			
			List<String> numlist = entry.getValue();
			
			Set<String> treeset = new TreeSet<String>();
			
			int i = numlist.size();
			
			for(int j = 0; j<i ; j++){
				
				List<String> wordlist = numTwords.get(numlist.get(j));
				treeset.addAll(wordlist);
 				
			}
			
			treeset.remove(word);
			
			Iterator<String> it = treeset.iterator();
			
//			int count = 0;
//			while(it.hasNext()){
//				doc.add(new Field("syn", it.next(), Field.Store.YES, Field.Index.NO));
//				count++;
//				if(count == 1)
//					break;
//			}
			if(it.hasNext())
				doc.add(new Field("syn", it.next(), Field.Store.YES, Field.Index.NO));
			
			//System.out.println(doc);
			doclist.add(doc);
			
		}
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
		return doclist;
		
	}

	//��dicת��
	private void dicToDmc(String dicdir, String dmcdir){
		
		try{
		
			BufferedReader reader = new BufferedReader( new FileReader( dicdir ) );
			BufferedWriter writer = new BufferedWriter( new FileWriter( dmcdir ) );
			
			String line = reader.readLine();
			
			while( line != null){
				
				if( line.length()>9){
					
					int n = line.indexOf('=');
					
					if(n>0){
					
						String num = line.substring(0, n);
						
						StringTokenizer st = new StringTokenizer(line.substring(n+1));
						
						while(st.hasMoreTokens()){
							
							writer.write(num + " " + st.nextToken() + "\r\n" );
							
						}
						
					}
					
				}
			
				line = reader.readLine();
				
		}
		
			
		writer.flush();
		writer.close();
		reader.close();
			
		}
		catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
}
