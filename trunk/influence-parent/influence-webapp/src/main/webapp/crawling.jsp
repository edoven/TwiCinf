<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>    
<%@ page import="java.io.File" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Properties" %>
<%@ page import="utils.HomePathGetter" %>


   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<p><a href="uploadCrawlingConfigFile.html"><img src="img/upload.png"  height="100" width="100">Upload crawling		 	configuration file</a></p>
	<form action="CrawlingLauncher">
	<p>output file name:<input type="text" name="fileName"></p>
	<%
		String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
		String folderPath = CRAWNKER_HOME+"crawling/config/";
		File folder = new File(folderPath);
		File[] files = folder.listFiles(); 		
		for (int i = 0; i < files.length; i++)  
			if (files[i].isFile()) 
			{
				String fileName = files[i].getName();
				String fileAbsolutePath = folderPath+fileName;			
				String checked;
				if (i==0)
					checked = "true";
				else
					checked = "false";
				out.println("<p>");
				out.println("<input type=\"radio\" name=\"configFilePath\" checked=\""+checked+"\" value=\""+fileAbsolutePath+"\">");
				out.println("<a href=\"FileViewer?file="+CRAWNKER_HOME+"crawling/config/"+fileName+"\">"+fileName+"</a>");
				//file delete
				out.println("(<a href=\"confirmDeletion.jsp?file="+CRAWNKER_HOME+"crawling/config/"+fileName+"\">DELETE</a>)");
				out.println("</p>");
			}	
	%>
	<input type="submit" value="Start crawling!">
	</form>
	
	<%
	String catalinaHomePath = System.getProperty("catalina.base");
	Properties properties = System.getProperties();
	if (catalinaHomePath==null)
		out.println("<p>Property catalina.base is not set. You can't see the logs. Please set it.</p>");
	else
		out.println("<p><a href=\"showLogFile.jsp?file="+catalinaHomePath+"/logs/twitter-crawling.log\">View log file!</a></p>");
	%>


</body>
</html>