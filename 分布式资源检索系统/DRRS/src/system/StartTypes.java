package system;
import javax.servlet.ServletContext;

import distributed.CommonTable;
import distributed.MainIPTable;
import search.Index;
public class StartTypes extends Thread{	
	private static boolean isRunning = false;   
	private ServletContext context = null; 
	//private Index index=new Index();
//	public String host;
//	public String remote;
     public StartTypes(ServletContext context){    
         this.context = context ;  
     } 
	@Override
	public void run(){        //ʵ�ַ���������ʱ���߳�
		// TODO Auto-generated method stub
		String host;
		String remote;
		//Index index;
		try{
			//index = new Index();
			host=Index.getHost();
			System.out.println(host);
			remote=Index.getRemote();
			System.out.println(remote);
			//index.indexDestory();
			if (! isRunning){  
				isRunning = true ;
				context.log("��ʼ�����ȡ�ļ�.") ; 
				if(host.equals(remote)){ //hostIP��remoteIP�����Ϊ��������
					System.out.println("��һ");
					MainIPTable main=new MainIPTable(host,remote);
					main.createIPTable();
					//main.addNewIP(host);
				}
				else{                   //hostIP��remoteIP�������Ϊһ�������
					CommonTable com=new CommonTable(host,remote);
					System.out.println("�ڶ�");
					com.createIPTable();
					com.registToRemote(host, remote);   //����������ע��
				}
			}
		}catch(Exception e){}
	}

}
