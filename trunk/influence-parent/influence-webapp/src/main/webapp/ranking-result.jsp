<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="it.cybion.influencers.ranking.RankedUser" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Insert title here</title>
	</head>
	<body>

		<%
        String rankedUsersFilename = (String)request.getAttribute("rankedUsersFilename");
        String rankedUsersOutputFilePath = (String)request.getAttribute("rankedUsersOutputFilePath");

		out.println("<p>influencers file results: " + rankedUsersOutputFilePath + "</p>");
        out.println("<a href=\"FileViewer?file="+rankedUsersOutputFilePath+"\">"+rankedUsersOutputFilePath+"</a>");
        %>

        <form action="InfluencersWriter" method="GET">

        <p>
        Click to get Ranked Users full profile:
        <select name="fileName">
          <option value="<%= rankedUsersFilename %>"><%= rankedUsersFilename %></option>
        </select>
        <br/>
        <input type="submit" value="Load full Influencers Profile">
        </form>

        <p></p>
        <%
		out.println("<p>screenName,followersCount,originalTweets,topicTweetsCount,topicTweetsRatio,AVGTopicRetweetsCount</p>");
		%>
		<p></p>

		<%
		List<RankedUser> rankedUsers = (List<RankedUser>)request.getAttribute("rankedUsers");

		for (RankedUser rankedUser : rankedUsers) {
			out.println("<p>"+rankedUser.toCSV()+"</p>");
        }
		%>

	</body>
</html>