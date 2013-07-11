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
		out.println("<p>screenName,followersCount,originalTweets,topicTweetsCount,topicTweetsRatio,AVGTopicRetweetsCount</p>");
		out.println("<p></p>");

		List<RankedUser> rankedUsers = (List<RankedUser>)request.getAttribute("rankedUsers");

		for (RankedUser rankedUser : rankedUsers) {
			out.println("<p>"+rankedUser.toCSV()+"</p>");
        }

        out.println("<br/>");
        out.println("<br/>");
        out.println("<br/>");

        String influencersAsJson = request.getAttribute("influencersJson");
        out.println("<p>"+influencersAsJson+"</p>");

		%>
	</body>
</html>