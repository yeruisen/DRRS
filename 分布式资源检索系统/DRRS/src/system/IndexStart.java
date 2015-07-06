package system;

import java.io.IOException;

import javax.servlet.ServletContext;

import search.Index;
//一个建索引的线程
public class IndexStart extends Thread{ 
	private ServletContext context = null; 
	 public IndexStart(ServletContext context){    
         this.context = context ;  
     } 
	@Override
	public void run(){
		
		@SuppressWarnings("unused")
		Process p1;
    	String s = "cmd /c C:\\OpenOffice\\program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\" -nofirststartwizard";
		try {
			p1 = Runtime.getRuntime().exec(s);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			
			Index index=new Index();
			
			try {context.log("开始执行指定任务建索引.") ;
				index.index();
				//index.indexDestory();
				context.log("执行指定任务建索引结束.") ;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
