<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,deal.*,java.net.URLEncoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/title.css">
<title>搜索结果</title>
</head>
<body>
<div id="total" style="	width:1200px;margin:auto;padding:auto;">
 <div id ="viewmain1">
<div id = "logo2"><img src="../img/logo2.jpg" /></div>
<br />
    	<a href="../HomePage.jsp">全部</a>
	  &nbsp;&nbsp;
        <a href="../Picture.jsp">图片</a>&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        <a href="../Vedio.jsp">视频</a>&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
		高级搜索
</div>
<br>
<br>
<br /><br /> &nbsp;&nbsp;&nbsp;
  <!---------------------------------------------------- 显示结果模块 ------------------------------------------->

 <%
  @SuppressWarnings("unchecked")
  Map<List<String>, Float> result=(Map<List<String>, Float>)session.getAttribute("result");
  if(result.size()!=0){
	  int time1=(Integer)session.getAttribute("time");%>
	   <div id="time"><font color="#666666" size="-1">找到<%=result.size()%>条结果,耗时约<%=time1%>毫秒</font></div>
<%
  }
  else{%>
	 <div id="time"><font color="#666666" size="-1">对不起！没有找到您要的结果，请您检查输入重新搜索!</font></div>
	  <%} %>
  <br>
  &nbsp;&nbsp;&nbsp;
  <a href="../Accurate.jsp">不满意,重新搜索</a>
  <br>
  <br>
  	 	<div id="totle">
<%
	PageModel pageModel1 = (PageModel)session.getAttribute("pageModel");
	int i = 0;
	System.out.println(pageModel1.page);
	 for (java.util.Map.Entry<List<String>, Float> entry : result.entrySet()) {
	 	if(i >=((pageModel1.page-1)*10)&&i<(pageModel1.getPage()*10)){
	 	%>
			<div id="result">
				<div id="result1">
	 	<a href="Re_direction?url5=<%=URLEncoder.encode(entry.getKey().get(2), "utf-8")%>" target = "_blank"><%=entry.getKey().get(0)%></a>
				</div>
				<div id="result2">
					<font size="-1" color="#666666">
    	<%=entry.getKey().get(1)%>
    				</font>
				</div>
			</div>
<%
	}
	i++;
}
 %>
 		</div>
<!----------------------------------------------------------- 分页模块 ---------------------------------------------------->
<div id = "page">
<div class="badoo"><span class="disabled">第${pageModel.page}页/共${pageModel.pageCount}页</span> 
<c:if test="${pageModel.page>1}">
 <a href="Control1?page=1">首页</a>
</c:if>
<c:if test="${pageModel.page>1}">
 <a href="Control1?page=${pageModel.prev}">上一页</a>
</c:if>

<c:forEach var="pre" items="${pageModel.prevPages }">
 <a href="Control1?page=${pre}">${pre}</a> 
</c:forEach>
<span class="current">${pageModel.page }</span>
<c:forEach var="next" items="${pageModel.nextPages }">
 <a href="Control1?page=${next}">${next}</a>
</c:forEach>

<c:if test="${pageModel.page<pageModel.last}">
 <a href="Control1?page=${pageModel.next}">下一页</a>
</c:if>
<c:if test="${pageModel.page<pageModel.last}">
 <a href="Control1?page=${pageModel.last}">尾页</a>
</c:if></div>
</div>
</div>
</body>
</html>