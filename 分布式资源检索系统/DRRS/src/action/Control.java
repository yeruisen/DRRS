package action;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutionException;

import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dom4j.DocumentException;



import deal.Change;
import deal.Identify;
import deal.PageModel;
import distributed.MainIPTable;
import search.*;
/**
 * Servlet implementation class Control
 */

public class Control extends HttpServlet {
	private static final long serialVersionUID = 1L;
	long start=System.currentTimeMillis();
	long end;
	String host = "";
	String remote = "";
	Timer timer = new Timer(true);
	boolean tag =false; 
	int count;
	static String num=new String();
    private MainIPTable main;
    /**
     * @see HttpServlet#HttpServlet()
     */
    
    public Control() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
    	    String host;
    	    String remote;
    	    System.out.println("�Ƿ�����ˣ���������");
    	try {
			//Index index=new Index();
			host=Index.getHost();
			this.host = host;
			String b[] = {"a","b","c","d"};
			remote=Index.getRemote();
			this.remote = remote;
			this.main=new MainIPTable(host,remote);
			if(host.equals(remote)){
				
				this.timer.schedule(new HostTime(host,remote,new MainIPTable(host,remote)),0,10000);
				try{
					this.main.search(null, b, b, "1");
					this.main.search("a", b, b, "1");
					this.main.search("a", b, b, "2");
					}
				catch(Exception e){
					
					e.printStackTrace();
					
				}
			}
			else{
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	}
    //�ڲ���
    class HostTime extends TimerTask{
		MainIPTable ma;
		 String host = null;
		 String remote = null;
		public HostTime(String host,String remote,MainIPTable ma){
			this.host = host;
			this.remote = remote;
			this.ma = ma;
		}
		int i= 0;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				System.out.println("��"+i+"��ִ�м�⵱������");
				System.out.println(tag);
				i++;
				if(tag == false){
					System.out.println("��"+i+"��ִ�м�⵱������");
					ma.checkDown();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
		 timer.cancel();
		 try {
			this.main.destroy();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = "D:/test.xml";
		File file = new File(s);
		if (file.exists()) {
			boolean d = file.delete();
			if (d) {
				this.getServletContext().log("ɾ���ļ��ɹ�");
				this.getServletContext().log("ע���ɹ�");
				System.out.println("���������з�Ӧ�𣿣�����");
			}
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			process(request,response);
			tag = false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void process(HttpServletRequest request,HttpServletResponse response)
	throws Exception{
		this.tag = true;
		System.out.println("���뵽��contorl");
		String[] kind=request.getParameterValues("kind");           //�õ����������ַ���
		RequestDispatcher rd;
		String forward = null;
		String fullpage = "1";
		int Long;													//�ϸ��ַ�������
		int i;														//����forѭ��
		String[] kind1;            									//ȥ���׸��ַ��������������
		Map<List<String>, Float> result = new LinkedHashMap<List<String>,Float>();//������������أ�
		String str=request.getParameter("kind1");					//�������������ת��Ϊ����
		Change change=new Change();
        count=change.change(str);            						//����������ת��Ϊ����
//        Search search=new Search();									//����search����
        HttpSession session1=request.getSession();
        session1.setAttribute("kind11", str);						//�浽session  control1����
        long startTime=0;
        long endTime=0;
        int time;
        if(count!=5){
        		//�������һҳ��ʱ��û���ύnum���ʵõ���numΪ�գ�ִ��length()�����
        		num=request.getParameter("num");	 					//�õ��ؼ����ַ���
        	if(num!=null){
				num=new String(num.getBytes("ISO8859-1"),"UTF-8");
				//����˫����
				String newnum = num.replace("\"", "&quot;");
				session1.setAttribute("num1", newnum);
				num=num.trim();										//ȥ����β�ո�
			}
        	if(num==null){
        		return;
        	}
			//����
			System.out.println(num);
			if(num!=null&&num.length()>38)
				num=num.substring(0, 38);
			if(num.length()==0){										//����ؼ����ַ�������Ϊ0��ˢ��
			  switch(count){
			  case 0:forward="../HomePage1.jsp";
			    break;
			  case 1:forward="../Picture1.jsp";
				break;
			  case 2:forward="../Music1.jsp";
				break;
			  case 3:forward="../Vedio1.jsp";
				break;
			  case 4:forward="../Document1.jsp";
				break;
			  }
		  }
		else{ 					   												//�������ַ���
			if(kind==null||kind.length==1){   												//ûѡ��ť 
				switch(count){
				  case 0:String[] a0={null,null,null,null};
				  		 startTime=(int)System.currentTimeMillis();
					     result=main.search(num,a0,null,fullpage);
				  		 //result=search.search(num, a0);
					     endTime=System.currentTimeMillis();
				         forward="../View.jsp";
				         break;
				  case 1:String[] a={"jpg","gif","jpeg","png"};
				         startTime=System.currentTimeMillis();
				         result=main.search(num, a,null,fullpage);
				         //result=search.search(num,a);
				         endTime=System.currentTimeMillis();
					     forward="../PictureView.jsp";
					     break;
				  case 2:String[] a1={"mp3","wav","wma","flac"};
				         startTime=System.currentTimeMillis();
				         result=main.search(num, a1,null,fullpage);
				         //result=search.search(num,a1);
				         endTime=System.currentTimeMillis();
					     forward="../MusicView.jsp";
					     break;
				  case 3:String[] a2={"avi","wmv","swf","asf","mp4"};
				  		 startTime=System.currentTimeMillis();
		                 result=main.search(num, a2,null,fullpage);
				  		//result=search.search(num,a2);
				  		 endTime=System.currentTimeMillis();
					     forward="../VedioView.jsp";
					     break;
				  case 4:fullpage = request.getParameter("fullpage");
				  		 System.out.println("ȫ�ļ����ֶ�"+fullpage);
					  	 String[] a3={"txt","doc","ppt","pdf"};
				         startTime=System.currentTimeMillis();
		                 result=main.search(num, a3,null,fullpage);
				         //result=search.search(num,a3);
				         endTime=System.currentTimeMillis();
					     forward="../DocumentView.jsp";
					     break;
				  }
			  }
			  else{              												//ѡ�˰�ť
				  Long=kind.length;
				  kind1=new String[Long-1];
				  for(i=1;i<Long;i++)
				  kind1[i-1]=kind[i];
				  switch(count){
				  case 0:String[] a0={null,null,null,null};
				         startTime=System.currentTimeMillis();
					     result=main.search(num,a0,null,fullpage);
				         //result=search.search(num,a0);
					     endTime=System.currentTimeMillis();
					     forward="../View.jsp";
				         break;
				  case 1:startTime=System.currentTimeMillis();
					     result=main.search(num, kind1,null,fullpage);
				          //result=search.search(num,kind1);
				  		endTime=System.currentTimeMillis();
					     forward="../PictureView.jsp";
					     break;
				  case 2:startTime=System.currentTimeMillis();
					     result=main.search(num, kind1,null,fullpage);
				         //result=search.search(num,kind1);
					     endTime=System.currentTimeMillis();
					     forward="../MusicView.jsp";
					     break;
				  case 3:startTime=System.currentTimeMillis();
					     result=main.search(num, kind1,null,fullpage);
				         // result=search.search(num,kind1);
					     endTime=System.currentTimeMillis();
					     forward="../VedioView.jsp";
					     break;
				  case 4:fullpage = request.getParameter("fullpage");
			  		 	 System.out.println("ȫ�ļ����ֶ�"+fullpage);
					  	 startTime=System.currentTimeMillis();
				         result=main.search(num, kind1,null,fullpage);
				         //result=search.search(num,kind1);
				         endTime=System.currentTimeMillis();
					     forward="../DocumentView.jsp";
					     break;
				  }
			  }
		}
	}
		else{
			boolean i1;															//�ж��Ƿ�����Ϊ��
			String[] num1;
			String accuratekind=request.getParameter("kind");					//�õ�������Դ����
		    num1=request.getParameterValues("num");
		    i1=Identify.identify(num1);
		    for(int m=0;m<num1.length;m++){
		    	//num1[m]=num1[m].replaceAll(" ","");
		    	num1[m]=num1[m].trim();
		    	//System.out.println("\n################\nKEYWORDS:"+num1[m]);
		    	num1[m]=new String(num1[m].getBytes("ISO8859-1"),"UTF-8");
		    }
		    if(i1){
		    	forward="../Accurate1.jsp";
		    }
		    else{
		    	int i2=change.change(accuratekind);
		    	switch(i2){
		    	case 0:String[] b={null};
		    	startTime=System.currentTimeMillis();
						 result=main.search(null,num1,b,fullpage);
						 endTime=System.currentTimeMillis();
		    	         forward="../AccurateView.jsp";
		    	         break;
		        case 1:String[] b1={"jpg","gif","jpeg","png","flac"};
		        		 startTime=System.currentTimeMillis();
   	                     result=main.search(null,num1,b1,fullpage);
   	                     endTime=System.currentTimeMillis();
   	                     forward="../AccurateView.jsp";
   	                     break;
		        case 2:String[] b2={"mp3","wav","wma","avi"};
		        		 startTime=System.currentTimeMillis();
   	                     result=main.search(null,num1,b2,fullpage);
   	                     endTime=System.currentTimeMillis();
   	                     forward="../AccurateView.jsp";
   	                     break;
		        case 3:String[] b3={"avi","wmv","swf","asf","mp4"};
		                 startTime=System.currentTimeMillis();
   	                     result=main.search(null,num1,b3,fullpage);
   	                     endTime=System.currentTimeMillis();
   	                     forward="../AccurateView.jsp";
   	                     break;
		        case 4:String[] b4={"txt","doc","ppt","pdf"};
		          		 startTime=System.currentTimeMillis();
   	              	     result=main.search(null,num1,b4,fullpage);
   	              	     endTime=System.currentTimeMillis();
   	              	     forward="../AccurateView.jsp";
   	              	    break;
		    	}
		    }
		}

        //---------------------------------------------��ҳ����-----------------------------------------
        long a=System.currentTimeMillis();
        PageModel pageModel = new PageModel();
        pageModel.setPageSize(10);// ����ҳ����ʾ��� ֵ
        pageModel.setTotalCount(result.size()); // ����������
        pageModel.setNum(5); // ���õ�ǰҳ��ǰ�����,/**ǰ�����ʾ5ҳ**/
        time=(int)(endTime-startTime);
        session1.setAttribute("time", time);
        session1.setAttribute("result", result);			//�����ս���浽session
        session1.setAttribute("pageModel", pageModel);
        session1.setAttribute("page", pageModel.getPage());
        long b=System.currentTimeMillis();
        System.out.println("���÷�ҳ ���ݶ���:"+(b-a));
    	rd= request.getRequestDispatcher(forward);
        rd.forward(request, response);
        end=System.currentTimeMillis();
        System.out.println("���������ࣺ"+(end-start));
  }
}