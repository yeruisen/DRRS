package action;

import java.io.IOException;

//import java.util.Map;



import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import deal.Change;
import deal.PageModel;

/**
 * Servlet implementation class Control1
 */
//@WebServlet("/Control1")
public class Control1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Control1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("ʹ�õ���doGet����");
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			System.out.println("ʹ�õ���Post");
			process(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void process(HttpServletRequest request,HttpServletResponse response)
			throws Exception{
		System.out.println("���뵽��contorl1");
		RequestDispatcher rd;
		Change change=new Change();
		HttpSession session1=request.getSession();
		//Map<org.apache.lucene.document.Document, Float> result=(Map<org.apache.lucene.document.Document,Float>)session1.getAttribute("result");
	      PageModel pageModel =(PageModel)session1.getAttribute("pageModel");
	        // ��õ�ǰҳ
	      System.out.println("��ȡ�˷�ҳ����");
	      String forward = null;
	      int page =pageModel.getPage();
	      String currentpage=request.getParameter("page");  //�õ������ҳ��
	      System.out.println("�õ���url�Ĵ�ֵpage="+currentpage);
	        try{
	        	page=Integer.parseInt(currentpage);
			    pageModel.setPage(page);

		}catch(Exception e){
			System.out.println("��ֵ����ת����������");
		}
	      pageModel.setPage(page);
	      System.out.println("�޸�ҳ��ɹ�");
	      String str=(String)session1.getAttribute("kind11");
	      int i=change.change(str);
	      switch(i){
		  case 0:forward="../View.jsp";
		    break;
		  case 1:forward="../PictureView.jsp";
			break;
		  case 2:forward="../MusicView.jsp";
			break;
		  case 3:forward="../VedioView.jsp";
			break;
		  case 4:forward="../DocumentView.jsp";
			break;
		  case 5:forward="../AccurateView.jsp";
			break;
		  }
	      rd= request.getRequestDispatcher(forward);
	      rd.forward(request, response);
	      System.out.println("��ת�ɹ�");
	}

}