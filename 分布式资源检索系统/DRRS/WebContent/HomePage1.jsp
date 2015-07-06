<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/title.css">
<title>搜索首页</title>
</head>
<body>
<div id="total" style="	width:1200px;height:500px;margin:auto;padding:auto;">
<center>
<div id="logo1"><img src="../img/logo1.jpg" /></div>
<div id = "main">
  <form action="${pageContext.request.contextPath}/servlet/Control" method="post">
  	<div id="word">
                          全部
        &nbsp;&nbsp;<a href="Picture.jsp">图片</a>&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        <a href="../Vedio.jsp">视频</a>&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
        <a href="../Accurate.jsp">高级搜索</a>
		</div>
    
    <br>
    <div id="input">
     <input type="text" name="num" value="" style = "width:100%; height:30px; font-size:18px;">
     </div>
     
     <div id="botten">
     <input type="submit" value="主义搜索" style="width:100%; height:36px; cursor:pointer;font-family:微软雅黑">
	 </div>
  </form>
 <input type="hidden" name="kind1" value="0">
  <input type="hidden" name="kind" value=" ">
  </div>
  </center>
  </div>
  </body>
</html>
