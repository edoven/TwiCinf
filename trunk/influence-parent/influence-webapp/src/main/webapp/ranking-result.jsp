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

        String outputFilePath = (String)request.getAttribute("outputFilePath");
		out.println("<p>influencers file results: " + outputFilePath + "</p>");

        out.println("<a href=\"FileViewer?file="+outputFilePath+"\">"+outputFilePath+"</a>");

		out.println("<p></p>");

		out.println("<p>screenName,followersCount,originalTweets,topicTweetsCount,topicTweetsRatio,AVGTopicRetweetsCount</p>");
		out.println("<p></p>");

		List<RankedUser> rankedUsers = (List<RankedUser>)request.getAttribute("rankedUsers");

		for (RankedUser rankedUser : rankedUsers) {
			out.println("<p>"+rankedUser.toCSV()+"</p>");
        }

		%>
	</body>
</html>