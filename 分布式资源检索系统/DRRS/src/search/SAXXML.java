package search;


import java.io.FileInputStream;
import java.net.URI;
//import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.TermVector;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXXML extends DefaultHandler{
private StringBuilder elementBuffer=new StringBuilder();	//用于记录元素内容
private Map<String,String> attributeMap;	//用于记录属性及属性值

private Document doc0;//用于普通检索
private Document doc1;//用于精确检索
private Document doc2;//用于全文检索

@SuppressWarnings("unchecked")
private List<Document> docs[]=new ArrayList[3];//文档列表

public SAXXML(){
	
	 docs[0] = new ArrayList<Document>();
	 docs[1] = new ArrayList<Document>();
	 docs[2] = new ArrayList<Document>();
	 
	 doc0 = new Document();//用于普通检索
	 doc1 = new Document();//用于精确检索
	 doc2 = new Document();//用于全文检索
	 
}

public List<Document>[] getDocuments(String datadir)throws Exception{
	//System.out.println("BEGIN");//***test
	SAXParserFactory spf=SAXParserFactory.newInstance();
	
	try{
		
		SAXParser parser=spf.newSAXParser();
		System.out.println("datadir:"+datadir);
		parser.parse(new InputSource(new FileInputStream(datadir)), this);
		
	}
	catch(Exception e){
		
		e.printStackTrace();
		
	}
	//System.out.println(doc);//*******test
	return docs;
}

 public void startDocument() {

 }

 public void endDocument() {
 }
 
public void startElement(String uri, String localName, String qName,Attributes atts) throws SAXException{
	
	elementBuffer.setLength(0);
	
	int numAtts = atts.getLength();
	if(numAtts>0){
		
		attributeMap = new HashMap<String,String>();
		
		for(int i = 0; i < numAtts; i++){
			
			attributeMap.put(atts.getQName(i), atts.getValue(i));
			
		}
	}
}

public void characters(char[] text,int start,int length){
	elementBuffer.append(text,start,length);
}

public void endElement(String uri, String localName, String qName)
   throws SAXException {
	
	if(qName.equals("allresource")){return;}
	else if(qName.equals("resourceitem")){
		
		//添加host，remote
		for(Entry<String,String> attribute: attributeMap.entrySet()){
			String attName=attribute.getKey();
			String attValue=attribute.getValue();
			//System.out.print("att:"+attName+":");//***test
			//System.out.println(attValue);//***test
			doc0.add(new Field(attName, attValue, Field.Store.YES, Field.Index.NO));
			doc1.add(new Field(attName, attValue, Field.Store.YES, Field.Index.NO));
			doc2.add(new Field(attName, attValue, Field.Store.YES, Field.Index.NO));
		}
		
		docs[0].add(doc0);
		docs[1].add(doc1);
		docs[2].add(doc2);
		
		//新建doc，用于保存下一个xml描述的文档
		doc0 = new Document();
		doc1 = new Document();
		doc2 = new Document();
	}
	else{
		if(qName.equals("id")){}
		else if(qName.equals("kind")) {
			doc0.add(new Field(qName, elementBuffer.toString().toLowerCase(), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			doc1.add(new Field(qName, elementBuffer.toString().toLowerCase(), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			doc2.add(new Field(qName, elementBuffer.toString().toLowerCase(), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
		}
		else{
			if(qName.equals("title")||qName.equals("describe")){
				doc0.add(new Field(qName, elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS));
				doc2.add(new Field(qName, elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS));
			}
			else if(qName.equals("url")){
				
				String url = elementBuffer.toString();
				//System.out.println("url:"+url);
				String urll = parseURL(url);
				
				doc0.add(new Field(qName, urll, Field.Store.YES, Field.Index.NO));
				doc2.add(new Field(qName, urll, Field.Store.YES, Field.Index.NO));
				doc2.add(new Field("filename", getFileName(url), Field.Store.YES, Field.Index.NO));  //用于获取文档名字
				
			}	
			else{
				doc0.add(new Field(qName, elementBuffer.toString(), Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
				doc2.add(new Field(qName, elementBuffer.toString(), Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
			}
			
			
			if(qName.equals("title"))
				doc1.add(new Field(qName, elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS));
			else if(qName.equals("author")||qName.equals("publisher"))
				doc1.add(new Field(qName, elementBuffer.toString().trim(), Field.Store.NO, Field.Index.NOT_ANALYZED_NO_NORMS));
			else if(qName.equals("keywords"))
				doc1.add(new Field(qName, elementBuffer.toString(), Field.Store.NO, Field.Index.ANALYZED_NO_NORMS));
			else if(qName.equals("describe"))
				doc1.add(new Field(qName, elementBuffer.toString(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS, TermVector.WITH_POSITIONS_OFFSETS));
			else if(qName.equals("url")){
				//System.out.println("url:"+elementBuffer.toString());
				String url = parseURL(elementBuffer.toString());
				doc1.add(new Field(qName, url, Field.Store.YES, Field.Index.NO));
			}
		}
	}
}

	private String parseURL(String url){		//对url进行转码
		
// 版本1
//		String url = "http://localhost:8080/TEST/我   呵%?#+&=呵  .doc";
//		System.out.println(url);
//		
//		String urls[] = url.split("/");
//		
//		String fileName = urls[urls.length-1];
//		System.out.println(fileName);
//		
//		for(int i = 0; i < urls.length; i++){
//			
//			urls[i] = urls[i].replace("%", "%25");
//			urls[i] = urls[i].replace(" ", "%20");
//			urls[i] = urls[i].replace("?", "%3F");
//			urls[i] = urls[i].replace("#", "%23");
//			urls[i] = urls[i].replace("+", "%2B");
//			urls[i] = urls[i].replace("&", "%26");
//			urls[i] = urls[i].replace("=", "%3D");
//			urls[i] = urls[i].replace("/", "%2F");
//			
//		}
//		
//		fileName = urls[urls.length-1];
//		System.out.println(fileName);
//
//		
//		url = "";
//		
//		url = url+urls[0]+"//";
//		for(int j = 2; j < urls.length-1; j++)
//			url = url+(new URI(urls[j])).toASCIIString()+"/";
//		
//		try{
//			
//			URI uri = new URI(fileName); 
//			url = url+uri.toASCIIString();
//			
//		}
//		catch(Exception e1){
//			
//			e1.printStackTrace();
//			
//		}

//版本2
//		String urls[] = url.split("/");
//		System.out.println(urls.length);
//		
//		String fileName = "";
//		for(int i = 4; i < urls.length-1; i++)
//			fileName = fileName + urls[i] + "/";
//		fileName = fileName + urls[urls.length-1];
//		
//		System.out.println("fileName:"+fileName);
//		
//		fileName = fileName.replace("%", "%25");
//		fileName = fileName.replace(" ", "%20");
//		fileName = fileName.replace("?", "%3F");
//		fileName = fileName.replace("#", "%23");
//		fileName = fileName.replace("+", "%2B");
//		fileName = fileName.replace("&", "%26");
//		fileName = fileName.replace("=", "%3D");
//		fileName = fileName.replace("/", "%2F");
//		
//		url = "";
//		
//		url = url+urls[0]+"//";
//		for(int j = 2; j < 4; j++)
//			url = url+urls[j]+"/";
//		
//		try{
//			
//			URI uri = new URI(fileName); 
//			url = url+uri.toASCIIString();
//			
//		}
//		catch(Exception e1){
//			
//			e1.printStackTrace();
//			
//		}
		
		
//版本3
		
		//System.out.println("url:"+url);
		
		String urlold = url;
		
		try{
			
			String urls[] = url.split("/");
			
			String fileName = urls[urls.length-1];
			
			fileName = fileName.replace("%", "%25");
			fileName = fileName.replace(" ", "%20");
			fileName = fileName.replace("?", "%3F");
			fileName = fileName.replace("#", "%23");
			fileName = fileName.replace("+", "%2B");
			fileName = fileName.replace("&", "%26");
			fileName = fileName.replace("=", "%3D");
			fileName = fileName.replace("/", "%2F");
			fileName = fileName.replace("\"", "%22");
			fileName = fileName.replace("(", "%28");
			fileName = fileName.replace(")", "%29");
			fileName = fileName.replace(",", "%2C");
			fileName = fileName.replace(":", "%3A");
			fileName = fileName.replace(";", "%3B");
			fileName = fileName.replace("<", "%3C");
			fileName = fileName.replace(">", "%3E");
			//fileName = fileName.replace("@", "");
			fileName = fileName.replace("|", "%7C");
			//fileName = fileName.replace("", "");
		
			url = "";
			
			url = url+urls[0]+"//";
			for(int j = 2; j < urls.length-1; j++)
				url = url+urls[j]+"/";
			
			
			try{
				
				URI uri = new URI(fileName); 
				fileName = uri.toASCIIString();
				url = url + fileName;
				
			}
			catch(Exception e1){
				
				//e1.printStackTrace();
				url = urlold;
				
			}
		
		}

		catch(Exception e){
			
			e.printStackTrace();
			url = urlold;
			
		}
		return url;
	}
	
	private String getFileName(String fileName){		//获取文件名,用于全文检索
		
		String str[] = fileName.split("/");
		fileName = str[str.length-1];
		return fileName;
		
	}

}
