<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="../css/title.css">
<title>高级搜索</title>
</head>
<body>
<div id="total" style="	width:1280px;height:800px;margin:auto;padding:auto;">
<center>
<div id="logo1"><img src="../img/logo1.jpg" /></div>
<div id="main1">
<form action="${pageContext.request.contextPath}/servlet/Control" method="post">
<table>
<input type="hidden" name="kind1" value="5">
        <a href="../HomePage.jsp">全部</a>&nbsp;&nbsp;
        <a href="../Picture.jsp">图片</a>&nbsp;&nbsp;
        <a href="../Music.jsp">音乐</a>&nbsp;&nbsp;
        <a href="../Vedio.jsp">视频</a>&nbsp;&nbsp;
        <a href="../Document.jsp">文档</a>&nbsp;&nbsp;
                        高级搜索
    <br />
	<br />
	<tr>
		<td width="146" height="40">标题中包含以下字调</td>
		<td width="10">&nbsp;</td>
		<td width="19">&nbsp;</td>
		<td width="192"><input type="text" name="num" value="" style = "width:100%; height:20px; font-size:15px;"></td>
	</tr>
		<tr>
		<td height="31">关键字包含以下字调</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><input type="text" name="num" value="" style = "width:100%; height:20px; font-size:15px;"></td>
	</tr>
		<tr>
		<td height="36">指定资源文件的作者</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><input type="text" name="num" value="" style = "width:100%; height:20px; font-size:15px;"></td>
	</tr>
	<tr>
		<td height="34">选择指定资源的格式</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>
	     <select name="kind" style = "width:100%; height:20px; text-align:center">
	     	<option value="0"[selected]>全部</option>
	     	<option value="1"[selected]>图片</option>
	     	<option value="2"[selected]>音乐</option>
	     	<option value="3"[selected]>视频</option>
	     	<option value="4"[selected]>文档</option>
	     </select>	     </td>
	     </tr>
	     <tr>
		<td height="37">指定资源文件出版社</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td><input type="text" name="num" value="" style = "width:100%; height:20px; font-size:15px;"></td>
		</tr>
		<tr>
		<td height="31">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		</tr>
</table>
		<div id="sou">
		<input name="submit" type="submit" value="主义搜索" style="width:75px; height:30px; cursor:pointer;font-family:微软雅黑"/></td>
		</div>
</form>
</div>
</center>
</div>
</body>
</html>