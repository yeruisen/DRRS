package action;


import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hslf.model.AutoShape;
import org.apache.poi.hslf.model.Fill;
import org.apache.poi.hslf.model.HeadersFooters;
import org.apache.poi.hslf.model.Shape;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
@SuppressWarnings("serial")
public class ConvertServlet extends HttpServlet {
	private File sourceFile;		//转换源文件
	private File pdfFile;			//PDF目标文件
	private File swfFile;			//SWF目标文件
	private Runtime r;				
	
	public void init() throws ServletException {

	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
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
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		//转换成pdf文件
		
		//获取url 从url提取name
		String url = request.getParameter("url");
		System.out.println("刚刚得到的:"+url);
		url = URLDecoder.decode(url,"UTF-8");
		System.out.println("转码后:"+url);
//		url = new String(url.getBytes("ISO8859-1"),"UTF-8");
		String[] b = url.split("/");
		String name = b[b.length-1];
		System.out.println("最初是这样的："+name);
//		name = new String(name.getBytes("ISO8859-1"),"UTF-8");
//		System.out.println("初次解码后："+name);
//		name = URLDecoder.decode(name,"UTF-8");
//		System.out.println("最后解码后："+name);
		
		int flag = 0;
		//准备删除文件
		String resourcesPath = request.getRealPath("/")+"flexpaper\\";
		File f = new File(resourcesPath);
		File[] files = f.listFiles();
		for(File ff : files){
			if(ff.getName().endsWith(".swf") && !ff.getName().equals("FlexPaperViewer.swf") && !ff.getName().equals("playerProductInstall.swf")){
				long start=System.currentTimeMillis();
				File file =new File(resourcesPath+"\\"+ff.getName());
		        long time =file.lastModified();
		        if((start - time) >5000){
		        	boolean n = file.delete();
		        	if(n){
		        		System.out.println("文件删除成功:"+ff.getName());
		        	}else{
		        		System.out.println("文件删除失败:"+ff.getName());
		        	}
		        }
			}
		}
		
		
		//构建路径开始
		
		String realPath = request.getRealPath("/");
		System.out.println(realPath);
		String[] a = realPath.split("\\\\");
		System.out.println("a数组");
		for(int i = 0;i < a.length;i++)
			System.out.println(a[i]);
		String path = "";
		for(int i = 0;i < a.length-1;i++)
			path = path + a[i] + "\\";
		String path10 = path;
		path = path + "resources\\"+name;
		//构建路径结束
		System.out.println("构建出来的路径:"+path);
		
		//以下随机生成文件名字
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < 7; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }
	    String swfName = sb.toString()+".swf";
		String pdfName = sb.toString()+".pdf";
		File testFile = new File(path);
		
		
		//大型ppt处理
		if((path.endsWith("ppt")||path.endsWith("pptx"))&&testFile.length() > 1433600){
			flag = 1;
			path10 = path10 + sb.toString()+".ppt";
			 SlideShow ppt = new SlideShow(); //创建ppt 
		        // 设置标题,底部信息    
		        HeadersFooters hdd = ppt.getSlideHeadersFooters();  
		        hdd.setSlideNumberVisible(true);  
		  
		        // add first slide  
		        Slide s[] = new Slide[5];//创建5张幻灯片 
		  
		        // set new page size  
		        ppt.setPageSize(new java.awt.Dimension(720, 560));  
		        // save changes  
		        FileOutputStream out = new FileOutputStream(path10); 
		        FileInputStream is = new FileInputStream(path);
		        SlideShow ppt1 = new SlideShow(is);
		        // get slides  
		        Slide[] slide1 = ppt1.getSlides();//得到每一张幻灯片  
		        
		        for (int i = 0; i < 5; i++) {
		        	s[i] = ppt.createSlide();
		        	s[i].setFollowMasterBackground(false);
		        	Fill file= s[i].getBackground().getFill();
		        	file.setFillType(Fill.FILL_SHADE);
		        	file.setBackgroundColor(slide1[i].getBackground().getFill().getBackgroundColor());
		        	file.setForegroundColor(slide1[i].getBackground().getFill().getForegroundColor());
		            Shape[] sh = slide1[i].getShapes();		//获取每一页元素
		            for (int j = 0; j < sh.length; j++) {
		            if (sh[j] instanceof AutoShape){
		               AutoShape shape = (AutoShape)sh[j];
		               s[i].addShape(shape);
		            } 
		            }
		        }
		        ppt.write(out);  
		        out.close();
		        
		}
		else{
			path10 = path;
		}
		
		System.out.println("############路径###########:"+path10);
		sourceFile = new File(path10);
		pdfFile = new File(request.getRealPath("/")+"flexpaper\\"+pdfName);
		swfFile = new File(request.getRealPath("/")+"flexpaper\\"+swfName);
		String path1 = request.getRealPath("/")+"flexpaper\\"+swfName;
		System.out.println("第一步：生成文件对象，准备转换");
		
		if(sourceFile.exists()) {
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
				try {
					connection.connect();
					DocumentConverter converter = new OpenOfficeDocumentConverter(connection);   
					converter.convert(sourceFile,pdfFile);
					pdfFile.createNewFile();
					connection.disconnect();  
					System.out.println("第二步：转换为PDF格式	路径" + pdfFile.getPath());
				} catch (java.net.ConnectException e) {
					e.printStackTrace();
					System.out.println("OpenOffice服务未启动");
					throw e;
				} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
					e.printStackTrace();
					System.out.println("读取文件失败");
					throw e;
				} catch (Exception e){
					e.printStackTrace();
					try {
						throw e;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
		} else {
			System.out.println("要转换的文件不存在");
		} 
		//转换成swf文件
		r = Runtime.getRuntime();

					if(pdfFile.exists()) {
						try {
							System.out.println("C:/SWFTools/pdf2swf.exe" + pdfFile.getPath() +
									" -o " + swfFile.getPath() + " -T 9");
							
							Process p = r.exec("C:/SWFTools/pdf2swf.exe" +" -t "+ '\"' +pdfFile.getPath() + '\"' + " -s "+"fashversion=9"+" -p "+"1-5"+" -o " + '\"' + swfFile.getPath() + '\"' + " -T 9");
							p.waitFor();
							
							//用waitfor()会导致进程阻塞，故采取以下办法解决阻塞
							InputStream is = p.getErrorStream(); // 获取ffmpeg进程的输出流
							BufferedReader br = new BufferedReader(new InputStreamReader(is)); // 缓冲读入
							StringBuilder buf = new StringBuilder(); // 保存ffmpeg的输出结果流
							String line = null;
							while((line = br.readLine()) != null) buf.append(line); // 循环等待ffmpeg进程结束
							System.out.println("ffmpeg输出内容为：" + buf);
							
							swfFile.createNewFile();
							System.out.println("第三步：转换为SWF格式	路径：" + swfFile.getPath());
							System.out.println("第si步：转换为SWF格式mingcheng：" + swfFile.getName());
							if(pdfFile.exists()) {
								pdfFile.delete();
							}
						} catch (Exception e) {
							e.printStackTrace();
							try {
								throw e;
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						} else {
						System.out.println("PDF文件不存在，无法转换");
					}
		if(flag == 1)
			sourceFile.delete();
		HttpSession session = request.getSession();
		session.setAttribute("fileName", swfFile.getName());
		session.setAttribute("adress", path1);
		System.out.println("我是测试:"+session.getAttribute("fileName"));
		response.sendRedirect(request.getContextPath()+"/Readfile.jsp");
	}
	}
