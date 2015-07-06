package action;

import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//import deal.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class Re_direction extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public Re_direction() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		doPost(request,response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		
		//---------------------------url处理-------------------------------------
		String url = request.getParameter("url5");
		System.out.println("刚刚得到的url:"+url);
		String[] a = url.split("/");
		String name = a[a.length-1];
		String ip = a[2];
		//转ASCII
//		URLEncodeer encoder = new URLEncodeer();
//		url = encoder.code(url);
//		System.out.println("第一次转化成ASCII码后:"+url);
		
		//解码
		String chUrl = URLDecoder.decode(url,"UTF-8");
		chUrl = chUrl.toLowerCase();
		System.out.println("URLDecoder初次解码后的:"+chUrl);
		
		//再次解码
//		url = new String(url.getBytes("ISO8859-1"),"utf-8");
//		System.out.println("getBytes解码后的:"+url);
		
		
		
		
		
		
//		String name = request.getParameter("name5");
//		name = new String(name.getBytes("ISO8859-1"),"UTF-8");
		
		
//		String[] a = chUrl.split("/");
//		String name = a[a.length-1];
//		a[a.length-1] = a[a.length-1].replace("%", "%25");
//		a[a.length-1] = a[a.length-1].replace(" ", "%20");
//		a[a.length-1] = a[a.length-1].replace("?", "%3F");
//		a[a.length-1] = a[a.length-1].replace("#", "%23");
//		a[a.length-1] = a[a.length-1].replace("+", "%2B");
//		a[a.length-1] = a[a.length-1].replace("&", "%26");
//		a[a.length-1] = a[a.length-1].replace("=", "%3D");
//		a[a.length-1] = a[a.length-1].replace("/", "%2F");
//		//转码操作
//		url = "";
//		for(int i = 0; i < a.length-1;i++){
//			url = url + a[i] + "/";
//		}
//		url = url + a[a.length-1];
//		try {
//			URI uri = new URI(url);
//			url = uri.toASCIIString();
//			System.out.println("转化成ASCII码后："+url);
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		//--------------------------------------文档定向--------------------------------------------
		if(chUrl.endsWith(".txt")||chUrl.endsWith(".pdf")||chUrl.endsWith(".doc")||
				chUrl.endsWith(".xls")||chUrl.endsWith(".xlsx")||chUrl.endsWith(".docx")||
				chUrl.endsWith(".ppt")||chUrl.endsWith(".pptx")){
			
			//String[] b = url.split("/");
			String path = "http://"+ip+"/DRRS/servlet/ConvertServlet?url="+url;//测试，改！！！
			System.out.println("跳转的url:"+path);
			System.out.println("encoderPath:"+URLEncoder.encode(path,"utf-8"));
		response.sendRedirect("http://"+ip+"/DRRS/servlet/ConvertServlet?url="+URLEncoder.encode(url, "utf-8"));
			}
			 	
			 	//---------------------------------------视频音乐定向--------------------------------------------
			 	
			 	else if(url.endsWith("mp3")||url.endsWith("wav")||url.endsWith("wma")||
			 			url.endsWith("flac")||url.endsWith("mp4")||url.endsWith("avi")||
			 			url.endsWith("wmv")||url.endsWith("swf")||url.endsWith("asf")){
			 		request.getRequestDispatcher("../VedioPlay.jsp?url1="+URLEncoder.encode(url, "utf-8")).forward(request, response);
			 	}
		
				//----------------------------------------图片定向-----------------------------------------------
			 	else if(url.endsWith("jpg")||url.endsWith("gif")||url.endsWith("jpeg")||
			 			url.endsWith("png")){
			 		Map<List<String>, Float> result = new LinkedHashMap<List<String>,Float>();
			 		List<String> list = new ArrayList<String>();
			 		list.add(name);
			 		list.add("no description");
			 		list.add(url);
			 		result.put(list,(float) 1.0);
			 		HttpSession session = request.getSession();
			 		session.setAttribute("result",result);
			 		request.getRequestDispatcher("../imgContains.jsp").forward(request, response);
			 	}
			 	else{
			 		request.getRequestDispatcher("../Error.jsp").forward(request, response);
			 	}
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
