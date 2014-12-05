<%-- 
    Document   : attentionUser_weibo_comment
    Created on : 2014-12-4, 18:03:42
    Author     : lenovo
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>sttentionUser_weibo_comment</title>
    </head>
    <body>
        <%
            //获取这条微博的评论数量
            Integer commentNum = (Integer) session.getAttribute("commentNum");

            //获取保存评论信息的list
            ArrayList<String[]> attentionUserWeiboComment = new ArrayList<String[]>();
            attentionUserWeiboComment = (ArrayList) session.getAttribute("attentionUserWeiboComment");

            //获取微博的id
            Integer weiboId = (Integer) session.getAttribute("commentWeiboId");
        %>
        <h2>您查询的这条微博的评论如下(评论内容，评论者姓名)：</h2>
        <%
            for (int i = 0; i < commentNum; i++) {
        %>
        <%=attentionUserWeiboComment.get(i)[2]%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=attentionUserWeiboComment.get(i)[1]%><br>
        <%
            }
        %>

        <div>
            <form method="post" name="frmLogin" action="SubmitCommentServlet">
                <h4>我要评论此微博（请在下面输入评论）</h4>
                <textarea class="form-control" rows="3" value="写评论" name="commentContent" onfocus="if (this.value == '写评论')
                            this.value = '';"></textarea><br>
                <input type="submit" name="Submit"　value="发评论" />
            </form>
        </div>
    </body>
</html>
