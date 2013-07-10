<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.File" %>
<%@ page import="utils.HomePathGetter" %>
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>
		<p><a href="uploadTweetsList.html">UPLOAD TWEETS LIST</a></p>
		<form action="TopicCreator" method="post">
		<p><input type="text" name="topicName" value="enter topic name ..."></p>		
		<%		
			String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
			File folder = new File(CRAWNKER_HOME+"ranking/topic/lists");
			File[] files = folder.listFiles();
			
			//let's get the topicFilesCount
			Integer topicFilesCount = 0;
			for (int i = 0; i < files.length; i++)  
				if (files[i].isFile()) 
					topicFilesCount++;
			session.setAttribute("topicFilesCount", topicFilesCount);
			out.println("<input type=\"hidden\" name=\"topicFilesCount\" value=\""+topicFilesCount+"\">");
			
			
			out.println("<p>In Topic:");
			int inTopicFileCount = 0;
			for (int i = 0; i < files.length; i++)  
				if (files[i].isFile()) 
				{
					String fileName = files[i].getName();
					String fileAbsolutePath = CRAWNKER_HOME+"ranking/topic/lists/"+fileName;
					String parameterName = "inTopic"+inTopicFileCount;
					inTopicFileCount++;
					out.println("<p>");
					out.println("<input type=\"checkbox\" name=\""+parameterName+"\" value=\""+fileAbsolutePath+"\">");
					out.println("<a href=\"FileViewer?file="+fileAbsolutePath+"\">"+fileName+"</a>");
					out.println("(<a href=\"confirmDeletion.jsp?file="+fileAbsolutePath+"\">DELETE</a>)");
					out.println("</p>");
			}
			out.println("</p>");
			

			folder = new File(CRAWNKER_HOME+"ranking/topic/lists");
			files = folder.listFiles();
			out.println("<p>Out of Topic:");
			int outTopicFileCount = 0;
			for (int i = 0; i < files.length; i++)  
				if (files[i].isFile()) 
				{
					String fileName = files[i].getName();
					String fileAbsolutePath = CRAWNKER_HOME+"ranking/topic/lists/"+fileName;
					String parameterName = "outOfTopic"+outTopicFileCount;
					outTopicFileCount++;
					out.println("<p>");
					out.println("<input type=\"checkbox\" name=\""+parameterName+"\" value=\""+fileAbsolutePath+"\">");
					out.println("<a href=\"FileViewer?file="+fileAbsolutePath+"\">"+fileName+"</a>");
					out.println("</p>");
				}	
			out.println("</p>");
		%>
		
		<p><input type="submit" value="Create topic"></p>
		
		</form>
	</body>
</html>