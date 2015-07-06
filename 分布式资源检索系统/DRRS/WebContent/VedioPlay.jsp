<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.net.URLDecoder" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>
		<%
//			String title = request.getParameter("title");
//			title = new String(title.getBytes("ISO8859-1"),"UTF-8");
//			title = URLDecoder.decode(title, "utf-8");
//			System.out.println("得到的名字:"+title);
//			title = new String(title.getBytes("ISO8859-1"),"utf-8");
//			title = URLDecoder.decode(title, "utf-8");
			out.println("在线播放");
			String url = request.getParameter("url1");
			
//			url = url.getBytes("ISO8859-1"),"UTF-8");
			System.out.println("视频播放最初得到的url:"+url);
 			url = URLDecoder.decode(url, "utf-8");
 			String[] a = url.split("/");
			String title = a[a.length-1];
//			System.out.println("视频播放最终得到的url:"+url);
		 %>
	</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
  <center>
  		<%=title %>
  		<br />
  		<%String browster = request.getHeader("User-Agent");
		if(browster.contains("MSIE")){
	 %>
    	<OBJECT id="myPlayer" codeBase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.
		cab#Version=6,4,0,53" type="application/x-oleobject" width="700" height="500" classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95" >
		<PARAM NAME="Filename" VALUE="<%=url%>">
		<PARAM NAME="ShowControls" VALUE="1"> <!-- 控制栏 -->
		<PARAM NAME="ShowStatusBar" VALUE="1">    <!-- 状态栏 -->
		</OBJECT>
		<%}else{ %>
		<video src="<%=url%>" controls="controls" width="700" height="500" autoplay="true">
		</video>
		<%} %>
		</center>
  </body>
</html>
