<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="utils.PropertiesLoader" %>
<%@ page import="it.cybion.influencers.ranking.RankedUser" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>

		<%
        /*

        String rankedUsersFilename = (String)request.getAttribute("rankedUsersFilename");
        String rankedUsersOutputFilePath = (String)request.getAttribute("rankedUsersOutputFilePath");

        out.println("<p>");
		out.println("click to see ranked results file: ");
        out.println("<a href=\"FileViewer?file="+rankedUsersOutputFilePath+"\">"+rankedUsersOutputFilePath+"</a>");
        out.println("</p>");

        */

        PropertiesLoader pl = new PropertiesLoader();
        String rankedUsersResultsDirectory = pl.getRankedUsersResultsDirectory();

        File rankedUsersResultsFile = new File(rankedUsersResultsDirectory);
        File[] rankedUsersFileNames = rankedUsersResultsFile.listFiles();
        %>

        <form action="InfluencersWriter" method="GET">
        <p>
        Click to load Ranked Users full profile from persistence and write to another json:
        <select name="rankedUsersFileName">

        <%

        for (int i = 0; i < rankedUsersFileNames.length; i++) {
            if (rankedUsersFileNames[i].isFile()) {
                String fileName = rankedUsersFileNames[i].getName();
                String fileAbsolutePath = rankedUsersResultsDirectory + fileName;
                %>
                <option value="<%= fileName %>"><%= fileName %></option>
                <%
            }
        }

        %>
        </select>
        <br/>
        <input type="submit" value="Load full Influencers Profile">
        </form>
	</body>
</html>