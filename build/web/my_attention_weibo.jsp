<%-- 
    Document   : my_attention_weibo
    Created on : 2014-12-3, 11:52:26
    Author     : lenovo
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>the weibo of the users I attention</title>
    </head>
    <body>
        <%
            Integer attentionWeiboNum = (Integer) session.getAttribute("attentionWeiboNum");

            ArrayList<String[]> aUserWeibo = new ArrayList<String[]>();
            aUserWeibo = (ArrayList) session.getAttribute("attentionUserWeibo");
        %>
        <h2>我关注的用户发过的微博如下：（用户名，微博内容，微博发送时间）</h2><br>
        <%=aUserWeibo.get(0)[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(0)[1]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(0)[3]%>&nbsp;&nbsp;&nbsp;&nbsp;
        <%String hyperlink = "CommentAttentionWeiboServlet?weiboID="+aUserWeibo.get(0)[2];%>
        <a href=<%=hyperlink%>><button type="button" class="btn btn-success">查看或评论此微博</button></a><br>
        <%
            for (int i = 1; i < attentionWeiboNum; i++) {
                if (aUserWeibo.get(i)[0].equals(aUserWeibo.get(i - 1)[0])) {
        %>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(i)[1]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(i)[3]%>&nbsp;&nbsp;&nbsp;&nbsp;
        <%String hyperlink1 = "CommentAttentionWeiboServlet?weiboID="+aUserWeibo.get(i)[2];%>
        <a href=<%=hyperlink1%>><button type="button" class="btn btn-success">查看或评论此微博</button></a><br>
        <%
                }else{
        %>
        <%=aUserWeibo.get(i)[0]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(i)[1]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=aUserWeibo.get(i)[3]%>&nbsp;&nbsp;&nbsp;&nbsp;
        <%String hyperlink2 = "CommentAttentionWeiboServlet?weiboID="+aUserWeibo.get(i)[2];%>
        <a href=<%=hyperlink2%>><button type="button" class="btn btn-success">查看或评论此微博</button></a><br>
        <%
                }
            }
        %>
    </body>
</html>
