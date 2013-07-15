<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>    
<%@ page import="java.io.File" %>
<%@ page import="utils.HomePathGetter" %>


   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="ScoresCalculationLauncher" method="GET">
		<%
			String CRAWNKER_HOME = HomePathGetter.getInstance().getHomePath();
			String usersListsFolderPath = CRAWNKER_HOME+"crawling/output/";
			File usersListsFolder = new File(usersListsFolderPath);
			File[] usersListsFiles = usersListsFolder.listFiles();
			out.println("<p>Users list files:");
			for (int i = 0; i < usersListsFiles.length; i++)  
				if (usersListsFiles[i].isFile()) 
				{
					String fileName = usersListsFiles[i].getName();
					String fileAbsolutePath = usersListsFolderPath+fileName;
					out.println("<p>");
					String checked;
					if (i==0)
						checked = "true";
					else
						checked = "false";
					out.println("<input type=\"radio\" checked=\""+checked+"\" name=\"usersListFilePath\" value=\""+fileAbsolutePath+"\">");
					out.println("<a href=\"FileViewer?file="+fileAbsolutePath+"\">"+fileName+"</a>");
					out.println("(<a href=\"confirmDeletion.jsp?file="+fileAbsolutePath+"\">DELETE</a>)");
					out.println("</p>");
				}	
			out.println("</p>");
		%>
		
		<p>From date: 
			<select name="fromDateDay">
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			  <option value="6">6</option>
			  <option value="7">7</option>
			  <option value="8">8</option>
			  <option value="9" selected="selected">9</option>
			  <option value="10">10</option>
			  <option value="11">11</option>
			  <option value="12">12</option>
			  <option value="13">13</option>
			  <option value="14">14</option>
			  <option value="15">15</option>
			  <option value="16">16</option>
			  <option value="17">17</option>
			  <option value="18">18</option>
			  <option value="19">19</option>
			  <option value="20">20</option>
			  <option value="21">21</option>
			  <option value="22">22</option>
			  <option value="23">23</option>
			  <option value="24">24</option>
			  <option value="25">25</option>
			  <option value="26">26</option>
			  <option value="27">27</option>
			  <option value="28">28</option>
			  <option value="29">29</option>
			  <option value="30">30</option>
			  <option value="31">31</option>
			</select>
			
			<select name="fromDateMonth">
			  <option value="1">january</option>
			  <option value="2">february</option>
			  <option value="3">march</option>
			  <option value="4">april</option>
			  <option value="5">may</option>
			  <option value="6">june</option>
			  <option value="7" selected="selected">july</option>
			  <option value="8">august</option>
			  <option value="9">september</option>
			  <option value="10">october</option>
			  <option value="11">november</option>
			  <option value="12">december</option>
			</select>
			
			<select name="fromDateYear">
			  <option value="2013">2013</option>
			  <option value="2012">2012</option>
			  <option value="2011">2011</option>
			  <option value="2010">2010</option>
			  <option value="2009">2009</option>
			</select>
		</p>
	
	
		<p>To date: 
			<select name="toDateDay">
			  <option value="1">1</option>
			  <option value="2">2</option>
			  <option value="3">3</option>
			  <option value="4">4</option>
			  <option value="5">5</option>
			  <option value="6">6</option>
			  <option value="7">7</option>
			  <option value="8">8</option>
			  <option value="9">9</option>
			  <option value="10" selected="selected">10</option>
			  <option value="11">11</option>
			  <option value="12">12</option>
			  <option value="13">13</option>
			  <option value="14">14</option>
			  <option value="15">15</option>
			  <option value="16">16</option>
			  <option value="17">17</option>
			  <option value="18">18</option>
			  <option value="19">19</option>
			  <option value="20">20</option>
			  <option value="21">21</option>
			  <option value="22">22</option>
			  <option value="23">23</option>
			  <option value="24">24</option>
			  <option value="25">25</option>
			  <option value="26">26</option>
			  <option value="27">27</option>
			  <option value="28">28</option>
			  <option value="29">29</option>
			  <option value="30">30</option>
			  <option value="31">31</option>
			</select>
			
			<select name="toDateMonth">
			  <option value="1">january</option>
			  <option value="2">february</option>
			  <option value="3">march</option>
			  <option value="4">april</option>
			  <option value="5">may</option>
			  <option value="6">june</option>
			  <option value="7" selected="selected">july</option>
			  <option value="8">august</option>
			  <option value="9">september</option>
			  <option value="10">october</option>
			  <option value="11">november</option>
			  <option value="12">december</option>
			</select>
			
			<select name="toDateYear">
			  <option value="2013">2013</option>
			  <option value="2012">2012</option>
			  <option value="2011">2011</option>
			  <option value="2010">2010</option>
			  <option value="2009">2009</option>
			</select>
		</p>
		
		<p>
		Topic (<a href="createTopic.jsp">CREATE NEW TOPIC</a>):
		
		<%
		String topicFolderPath = CRAWNKER_HOME+"ranking/topic/";
		File topicFolder = new File(topicFolderPath);
		File[] topicFiles = topicFolder.listFiles();
		for (int i = 0; i < topicFiles.length; i++)  
		{
			if (topicFiles[i].isFile()) 
			{
				String topicFileName = topicFiles[i].getName();							
				out.println("<p>");
				String checked;
				if (i==0)
					checked = "true";
				else
					checked = "false";
				out.println("<input type=\"radio\" checked=\""+checked+"\" name=\"topicFile\" value=\""+(topicFolderPath+topicFileName)+"\">");
				out.println("<a href=\"FileViewer?file="+topicFolderPath+topicFileName+"\">"+topicFileName+"</a>");
				out.println("(<a href=\"confirmDeletion.jsp?file="+topicFolderPath+topicFileName+"\">DELETE</a>)");
				out.println("</p>");
			}	
		}
		out.println("</p>");
		%>
		
		tweetsPerDocument: <input type="text" name="tweetsPerDocument" value="50"><br>
		k (for knn): <input type="text" name="k" value="10"><br>

		
		<input type="submit" value="Calculate the Scores!">
	</form>

</body>
</html>