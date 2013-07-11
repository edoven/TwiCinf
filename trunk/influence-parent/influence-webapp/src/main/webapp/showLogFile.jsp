<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.FileReader" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="utils.Tail" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<%
		String filePath = request.getParameter("file");
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
	    }
		List<String> lines = Tail.getLastLines(filePath,100);
		for (int i=0; i<lines.size(); i++)
			out.println(lines.get(lines.size()-1-i) + "</br>");
		%>
	</body>
</html>