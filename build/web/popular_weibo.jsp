<%-- 
    Document   : popular_weibo
    Created on : 2014-12-3, 19:46:45
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>most popular weibo</title>
    </head>
    <body>
        <%
            Integer popularWeiboNum = (Integer) session.getAttribute("popularWeiboNum");

            String[] userName = new String[popularWeiboNum];
            String[] weiboContent = new String[popularWeiboNum];
            String[] weiboTime = new String[popularWeiboNum];
            String[] commentContent = new String[popularWeiboNum];
            Integer[] commentNum = new Integer[popularWeiboNum];
            for (int m = 1, i = 0; m <= popularWeiboNum; m++, i++) {
                String aUserName = "pUserName" + m;
                String wContent = "popularWeiboContent" + m;
                String wTime = "popularWeiboTime" + m;
                String popularComment = "popularComment" + m;
                String commentNumber = "pContentNum" + m;
                userName[i] = (String) session.getAttribute(aUserName);
                weiboContent[i] = (String) session.getAttribute(wContent);
                weiboTime[i] = (String) session.getAttribute(wTime);
                commentContent[i] = (String) session.getAttribute(popularComment);
                commentNum[i] = (Integer) session.getAttribute(commentNumber);
            }
        %>
        
        <h2>评论数量在前N名的微博及其评论如下：（用户名，微博内容，微博发送时间，本微博评论内容，本微博总评论数量）</h2><br>
        <%=userName[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=weiboContent[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=weiboTime[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=commentContent[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=commentNum[0]%><br>
        <%
            for (int i = 1; i < popularWeiboNum; i++) {
                if(weiboContent[i].equals(weiboContent[i-1])){
        %>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=commentContent[i]%><br>
        <%
                }else{
        %>
        <%=userName[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=weiboContent[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=weiboTime[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=commentContent[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=commentNum[i]%><br>
        <%
                }
            }
        %>
    </body>
</html>
