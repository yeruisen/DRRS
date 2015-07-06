<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,deal.*,java.net.URL,java.awt.image.BufferedImage,java.net.URLDecoder,java.net.URI,java.net.URLEncoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/title.css">
<title>
<%
String num=(String)session.getAttribute("num1");
out.println("图片--"+num);
%>
</title>
</head>


<body>
<div id="total" style="	width:1200px;margin:auto;padding:auto;">
<!----------------------------------------- 搜索模块 ---------------------------------------------->

<div id ="viewmain">
<div id = "logo2"><img src="../img/logo2.jpg" /></div>
<br />&nbsp;&nbsp;
    	 <a href="../HomePage.jsp">全部</a>&nbsp;&nbsp;
        	图片&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        <a href="../Vedio.jsp">视频</a>&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
        <a href="../Accurate.jsp">高级搜索</a>
        <br /><br />
    <form action="${pageContext.request.contextPath}/servlet/Control" method="post">
    <br>
	<div id = "viewinput">
    <input type="text" name="num" value="<% out.println(num); %>" style = "width:100%; height:25px; font-size:18px; text-shadow:#999999;">
	</div>
	<div id="viewbotten">
    <input type="submit" value="主义搜索" style="width:100%; height:31px; cursor:pointer;font-family:微软雅黑">
	</div>
	</div>
    <br /><br /><br /><br /><br /><br /><br /><br />
    <input type="hidden" name="kind1" value="1">
    <input type="hidden" name="kind" value="">
    <div id = "kind">
    <input type="checkbox" name="kind" value="jpg">JPG
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="gif">GIF
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="jpeg">JPEG
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="png">PNG
    </div>
  </form>
  <br />
    <!---------------------------------------------------- 显示结果模块 ------------------------------------------->
 <%
  @SuppressWarnings("unchecked")
  Map<List<String>, Float> result=(Map<List<String>, Float>)session.getAttribute("result");
  if(result.size()!=0){
	  int time1=(Integer)session.getAttribute("time");if(num.length()>38){
	  %>
	   <div id="time"><font color="#666666" size="-1">输入的字符长度大于38，"<%=num.charAt(38)%>"后的字符都被忽略
	   &nbsp;&nbsp;&nbsp;
	   找到<%=result.size()%>条结果,耗时约<%=time1%>毫秒</font></div>
	   <%}else{%>
	   <div id="time"><font color="#666666" size="-1">找到<%=result.size()%>条结果,耗时约<%=time1%>毫秒</font></div>
	   <% } %>
<%
  }
  else{%>
	 <div id="time"><font color="#666666" size="-1">对不起！没有找到您要的结果，请您检查输入重新搜索!</font></div>
	  <%}
	   if(num.length()>38){
	   %>
	  <%
  }
  %>
    <div id="main20">
    	<ul>
    	  	<% 
    	  	String browster = request.getHeader("User-Agent");
    	  	System.out.println("浏览器："+browster);
    	  	int i = result.size();
    	  	int height[] = new int[i];
    	  	int width[] = new int[i];
    	  	String url[] = new String[i];
    	  	int j = 0;
    	  	
    	  	 for (java.util.Map.Entry<List<String>, Float> entry : result.entrySet()) {
 	    	  	url[j] = entry.getKey().get(2);
 	    	  	System.out.println("url:"+url[j]);
// 	    	  	System.out.println("url:"+URLDecoder.decode(url[j],"utf-8"));
// 	    	  	System.out.println("title"+entry.getKey().get(0));
// 	    	  	System.out.println("URL:"+(new URL(url[j])));
//				String fileName = "卡奈玛国家公园1.jpg";
 /* 				fileName = fileName.replace("%", "%25");
				fileName = fileName.replace(" ", "%20");
				fileName = fileName.replace("?", "%3F");
				fileName = fileName.replace("#", "%23");
				fileName = fileName.replace("+", "%2B");
				fileName = fileName.replace("&", "%26");
				fileName = fileName.replace("=", "%3D");
				fileName = fileName.replace("/", "%2F");  */
/* 				URI uri = new URI(fileName); 
				fileName = uri.toASCIIString(); */
//				fileName = URLEncoder.encode(fileName,"utf-8");
//				URI uri = new URI(fileName); 
//				fileName = uri.toASCIIString(); 
//	    	  	url[j] = "http://localhost:8080/resources/"+fileName;
//	    	  	System.out.println("url:"+url[j]);
//	    	  	url[j] = URLEncoder.encode(url[j]);
	    	  	
	    	  	BufferedImage bi = javax.imageio.ImageIO.read(new URL(url[j]));
	    	 	width[j] = bi.getWidth();
	    	 	height[j] = bi.getHeight();
	    	  	width[j] = (width[j]<350)?width[j]:350;
	    	  	height[j] = (height[j]<191)?height[j]:191;
	    	  	j++;
	    	  	
    	  	}
    	  	
    	  	
    	  	for(int k = 0; k < i; k++){
    	  		int c = k;
    	  		for(j = k+1; j < i; j++){
    	  			if(height[c] > height[j]){
    	  				c = j;
    	  				System.out.println(c);
    	  			}
    	  		
    	  		}
    	  		
				if(c != k){
				
					int m = width[k];
					width[k] = width[c];
					width[c] = m;
					
					m = height[k];
					height[k] = height[c];
					height[c] = m;
					
					String u = url[k];
					url[k] = url[c];
					url[c] = u; 
				}
				
    	  	}	
    	  	
  			 %>
  			 <%for(j = 0; j < i; j++){ %>
    		<li style="list-style-type:none; float:left; width:<%=width[j]+5%>px; height: 191px;margin:2px 2px 2px 2px">
    			<div style="width:<%=width[j]+5%>px;height:<%=191%>px;text-align:center;position:relative">
    				<a href="../imgContains.jsp" target = "_blank">
		    			<img src="<%=url[j]%>" style="width:<%=width[j]%>px; height:<%=height[j]%>px; 
		    			visibility: visible; background: none; padding-top:<%=(191-height[j])/2%>px" />
		    		</a>
    			</div>
    		</li>
    		<%} %>
    		 
    	</ul>
    </div>
    </div>
</body>
</html>