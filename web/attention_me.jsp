<%-- 
    Document   : attention_me
    Created on : 2014-12-3, 10:38:24
    Author     : lenovo
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>the users attention me</title>
    </head>
    <body>
        <%
            Integer attentionMeUserNum = (Integer) session.getAttribute("attentionMeUserNum");

            String[] userName = new String[attentionMeUserNum];
            String[] userEmail = new String[attentionMeUserNum];
            String[] userDescription = new String[attentionMeUserNum];
            for (int m = 1, i = 0; m <= attentionMeUserNum; m++, i++) {
                String amUserName = "amUserName" + m;
                String amUserEmail = "amUserEmail" + m;
                String amUserDescription = "amUserDescription" + m;
                userName[i] = (String) session.getAttribute(amUserName);
                userEmail[i] = (String) session.getAttribute(amUserEmail);
                userDescription[i] = (String) session.getAttribute(amUserDescription);
            }
        %>

        <h2>关注我的用户信息如下(用户名，email，用户简介)：</h2><br>
        <%
            for (int i = 0; i < attentionMeUserNum; i++) {
        %>
        <%=userName[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=userEmail[i]%>&nbsp;&nbsp;&nbsp;&nbsp;<%=userDescription[i]%><br>
        <%
            }
        %>
    </body>
</html>
