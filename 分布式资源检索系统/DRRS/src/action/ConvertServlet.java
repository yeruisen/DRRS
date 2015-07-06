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
	private File sourceFile;		//ת��Դ�ļ�
	private File pdfFile;			//PDFĿ���ļ�
	private File swfFile;			//SWFĿ���ļ�
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
		//ת����pdf�ļ�
		
		//��ȡurl ��url��ȡname
		String url = request.getParameter("url");
		System.out.println("�ոյõ���:"+url);
		url = URLDecoder.decode(url,"UTF-8");
		System.out.println("ת���:"+url);
//		url = new String(url.getBytes("ISO8859-1"),"UTF-8");
		String[] b = url.split("/");
		String name = b[b.length-1];
		System.out.println("����������ģ�"+name);
//		name = new String(name.getBytes("ISO8859-1"),"UTF-8");
//		System.out.println("���ν����"+name);
//		name = URLDecoder.decode(name,"UTF-8");
//		System.out.println("�������"+name);
		
		int flag = 0;
		//׼��ɾ���ļ�
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
		        		System.out.println("�ļ�ɾ���ɹ�:"+ff.getName());
		        	}else{
		        		System.out.println("�ļ�ɾ��ʧ��:"+ff.getName());
		        	}
		        }
			}
		}
		
		
		//����·����ʼ
		
		String realPath = request.getRealPath("/");
		System.out.println(realPath);
		String[] a = realPath.split("\\\\");
		System.out.println("a����");
		for(int i = 0;i < a.length;i++)
			System.out.println(a[i]);
		String path = "";
		for(int i = 0;i < a.length-1;i++)
			path = path + a[i] + "\\";
		String path10 = path;
		path = path + "resources\\"+name;
		//����·������
		System.out.println("����������·��:"+path);
		
		//������������ļ�����
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
		
		
		//����ppt����
		if((path.endsWith("ppt")||path.endsWith("pptx"))&&testFile.length() > 1433600){
			flag = 1;
			path10 = path10 + sb.toString()+".ppt";
			 SlideShow ppt = new SlideShow(); //����ppt 
		        // ���ñ���,�ײ���Ϣ    
		        HeadersFooters hdd = ppt.getSlideHeadersFooters();  
		        hdd.setSlideNumberVisible(true);  
		  
		        // add first slide  
		        Slide s[] = new Slide[5];//����5�Żõ�Ƭ 
		  
		        // set new page size  
		        ppt.setPageSize(new java.awt.Dimension(720, 560));  
		        // save changes  
		        FileOutputStream out = new FileOutputStream(path10); 
		        FileInputStream is = new FileInputStream(path);
		        SlideShow ppt1 = new SlideShow(is);
		        // get slides  
		        Slide[] slide1 = ppt1.getSlides();//�õ�ÿһ�Żõ�Ƭ  
		        
		        for (int i = 0; i < 5; i++) {
		        	s[i] = ppt.createSlide();
		        	s[i].setFollowMasterBackground(false);
		        	Fill file= s[i].getBackground().getFill();
		        	file.setFillType(Fill.FILL_SHADE);
		        	file.setBackgroundColor(slide1[i].getBackground().getFill().getBackgroundColor());
		        	file.setForegroundColor(slide1[i].getBackground().getFill().getForegroundColor());
		            Shape[] sh = slide1[i].getShapes();		//��ȡÿһҳԪ��
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
		
		System.out.println("############·��###########:"+path10);
		sourceFile = new File(path10);
		pdfFile = new File(request.getRealPath("/")+"flexpaper\\"+pdfName);
		swfFile = new File(request.getRealPath("/")+"flexpaper\\"+swfName);
		String path1 = request.getRealPath("/")+"flexpaper\\"+swfName;
		System.out.println("��һ���������ļ�����׼��ת��");
		
		if(sourceFile.exists()) {
				OpenOfficeConnection connection = new SocketOpenOfficeConnection(8100);
				try {
					connection.connect();
					DocumentConverter converter = new OpenOfficeDocumentConverter(connection);   
					converter.convert(sourceFile,pdfFile);
					pdfFile.createNewFile();
					connection.disconnect();  
					System.out.println("�ڶ�����ת��ΪPDF��ʽ	·��" + pdfFile.getPath());
				} catch (java.net.ConnectException e) {
					e.printStackTrace();
					System.out.println("OpenOffice����δ����");
					throw e;
				} catch (com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException e) {
					e.printStackTrace();
					System.out.println("��ȡ�ļ�ʧ��");
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
			System.out.println("Ҫת�����ļ�������");
		} 
		//ת����swf�ļ�
		r = Runtime.getRuntime();

					if(pdfFile.exists()) {
						try {
							System.out.println("C:/SWFTools/pdf2swf.exe" + pdfFile.getPath() +
									" -o " + swfFile.getPath() + " -T 9");
							
							Process p = r.exec("C:/SWFTools/pdf2swf.exe" +" -t "+ '\"' +pdfFile.getPath() + '\"' + " -s "+"fashversion=9"+" -p "+"1-5"+" -o " + '\"' + swfFile.getPath() + '\"' + " -T 9");
							p.waitFor();
							
							//��waitfor()�ᵼ�½����������ʲ�ȡ���°취�������
							InputStream is = p.getErrorStream(); // ��ȡffmpeg���̵������
							BufferedReader br = new BufferedReader(new InputStreamReader(is)); // �������
							StringBuilder buf = new StringBuilder(); // ����ffmpeg����������
							String line = null;
							while((line = br.readLine()) != null) buf.append(line); // ѭ���ȴ�ffmpeg���̽���
							System.out.println("ffmpeg�������Ϊ��" + buf);
							
							swfFile.createNewFile();
							System.out.println("��������ת��ΪSWF��ʽ	·����" + swfFile.getPath());
							System.out.println("��si����ת��ΪSWF��ʽmingcheng��" + swfFile.getName());
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
						System.out.println("PDF�ļ������ڣ��޷�ת��");
					}
		if(flag == 1)
			sourceFile.delete();
		HttpSession session = request.getSession();
		session.setAttribute("fileName", swfFile.getName());
		session.setAttribute("adress", path1);
		System.out.println("���ǲ���:"+session.getAttribute("fileName"));
		response.sendRedirect(request.getContextPath()+"/Readfile.jsp");
	}
	}
