<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,java.net.URLEncoder" %>
<%@page import="deal.PageModel" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/title.css">
<title>
<%
    String num=(String)session.getAttribute("num1");
    out.println(num);
%>
</title>
</head>
<!----------------------------------------- 搜索模块 ---------------------------------------------->

<body>
<div id="total" style="	width:1200px;margin:auto;padding:auto;">
<div id ="viewmain">
<div id = "logo2"><img src="../img/logo2.jpg" /></div>
<br />&nbsp;&nbsp;
      全部
	  &nbsp;&nbsp;
        <a href="../Picture.jsp">图片</a>&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        <a href="../Vedio.jsp">视频</a>&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
        <a href="../Accurate.jsp">高级搜索</a>
        <br /><br />
    <form action="${pageContext.request.contextPath}/servlet/Control" method="post">
    <br>
	<div id = "viewinput">
    <input type="text" name="num" value="<%out.println(num); %>" style = "width:100%; height:25px; font-size:18px; text-shadow:#999999;">
	</div>
	<div id="viewbotten">
    <input type="submit" value="主义搜索" style="width:100%; height:31px; cursor:pointer; font-family:微软雅黑">
	</div>
    <input type="hidden" name="kind1" value="0">
    <input type="hidden" name="kind" value="">
  </form>
  </div>
  <br />
<!----------------------------------------------- 显示结果模块------------------------------------------------------------->
 <%
  @SuppressWarnings("unchecked")
  Map<List<String>, Float> result=(Map<List<String>, Float>)session.getAttribute("result");
  if(result.size()!=0){
	  int time1=(Integer)session.getAttribute("time");
	  if(num.length()>38){
	  %>
	   <div id="time"><font color="#666666" size="-1">输入的字符长度大于38，"<%=num.charAt(38)%>"后的字符都被忽略
	   &nbsp;&nbsp;&nbsp;
	   找到<%=result.size()%>条结果,耗时约<%=time1%>毫秒</font></div>
	   <%}else{%>
	   <div id="time"><font color="#666666" size="-1">找到<%=result.size()%>条结果,耗时约<%=time1%>毫秒</font></div>
	   <% } %>
<%
  }else{%>
	 <div id="time"><font color="#666666" size="-1">对不起！没有找到您要的结果，请您检查输入重新搜索!</font></div>
	  <%}
if(num.length()>38){
	   %>
	  <%}%>
<br />
<br />
<br />
<br />
 <div id = "null"></div>
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
<br>
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