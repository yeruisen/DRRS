package distributed;

import java.io.File;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.SAXReader;

import search.Index;


public class UpdateIP {

	private String remote = null;

	public UpdateIP(String remote) {
		this.remote = remote;
	}
	
	//»ñµÃtest.xmlÂ·¾¶
	private String getXMLPath(){
		System.out.println("#########getXMLPath#########");
		Index in = new Index(1);
		//System.out.println(in.getXmlPath()+"\n############\n");
		return in.getXmlPath();
		
	}

	public void runs() {
		SAXReader reader1 = new SAXReader();
		Document doc1;
		try {
			doc1 = reader1.read(new File(this.getXMLPath()));
			Element root = doc1.getRootElement();
			root.attribute("remote").setValue(this.remote);
			java.io.OutputStream out=new java.io.FileOutputStream(new File(this.getXMLPath()));
			  java.io.Writer wr=new java.io.OutputStreamWriter(out,"UTF-8");  
			  doc1.write(wr);  
			  wr.close();
			  out.close();
		} catch (DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
