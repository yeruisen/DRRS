<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ page import="java.util.*,deal.*,java.net.URLEncoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="../css/title.css">
<title>
<%
String num=(String)session.getAttribute("num1");
out.println("视频--"+num);
%>
</title>
</head>
  <link rel="stylesheet" href="../css/a.css">
<body>
<div id="total" style="	width:1200px;margin:auto;padding:auto;">
<!----------------------------------------- 搜索模块 ---------------------------------------------->

<div id ="viewmain">
<div id = "logo2"><img src="../img/logo2.jpg" /></div>
<br />&nbsp;&nbsp;
		<a href="../HomePage.jsp">全部</a>&nbsp;&nbsp;
		<a href="../Picture.jsp">图片</a>&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        	视频&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
        <a href="../Accurate.jsp">高级搜索</a>
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
    <input type="hidden" name="kind1" value="3">
    <input type="hidden" name="kind" value="">
    <div id = "kind">
    <input type="checkbox" name="kind" value="avi">AVI
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="wmv">WMV
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="swf">SWF
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    <input type="checkbox" name="kind" value="asf">ASF
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

  <!--------------------------------------------------------- 遍历结果 ----------------------------------------->
<%
	 for (java.util.Map.Entry<List<String>, Float> entry : result.entrySet()) {
	 	%>
	 	
<div id = "main">
	<div id = "body">
	<%String browster = request.getHeader("User-Agent");
	if(browster.contains("MSIE")){
	 %>
		<OBJECT id="myPlayer" codeBase="http://activex.microsoft.com/activex/controls/mplayer/en/nsmp2inf.
		cab#Version=6,4,0,53" type="application/x-oleobject" width="200" height="200" classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95" >
		<PARAM NAME="Filename" VALUE="#">
		<PARAM NAME="ShowControls" VALUE="0">
		<PARAM NAME="autoStart" VALUE="0">
		</OBJECT>
		<%}else{%>
		<video src="#" controls="controls" width="200" height="200" >
		</video>
		<%} %>
		</div>
	<div id = "title">
		<a href="../VedioPlay.jsp?url1=<%=URLEncoder.encode(entry.getKey().get(2), "utf-8")%>" target = "_blank"><%=entry.getKey().get(0)%></a>
		</div>
	</div>
<%
	}
 %>
 </div>
</body>
</html>