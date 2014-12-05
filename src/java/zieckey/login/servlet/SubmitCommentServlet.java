/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zieckey.login.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lenovo
 */
@WebServlet(name = "SubmitCommentServlet", urlPatterns = {"/SubmitCommentServlet"})
public class SubmitCommentServlet extends HttpServlet {

    public SubmitCommentServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        String result = "";

        // 获取评论内容
        String commentContent = request.getParameter("commentContent");
        if (commentContent == "" || commentContent == null || commentContent.length() > 100) {
            try {
                result = "请重新输入微博（不超过200字符）！";
                request.setAttribute("ErrorWeibo", result);
                response.sendRedirect("login_success.jsp");
            } catch (Exception e) {
            }
        }

        //获取用户名
        String username = (String) request.getSession().getAttribute("UserName");

        //获取微博id
        Integer weiboId = (Integer) session.getAttribute("commentWeiboId");

        // 登记JDBC驱动程序
        try {
            Class.forName("org.postgresql.Driver").newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("InstantiationException");
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("IllegalAccessException");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            System.out.println("ClassNotFoundException");
        }

        // 连接参数与Access不同
        String url = "jdbc:postgresql://localhost:5432/Twitter";

        // 建立连接
        java.sql.Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = DriverManager.getConnection(url, "postgres", "postgres");
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            String sqlCommentNum = "select comment_id from weibo.comment order by comment_id DESC LIMIT 1";
            rs = stmt.executeQuery(sqlCommentNum);// 返回查询结果(现有的评论总条数)
            rs.next();
            int commentNum = rs.getInt("comment_id");
            commentNum++;

            //查询用户user_id
            String sqlUserId = "select user_id from weibo.user where user_name = '" + username + "'";
            rs = stmt.executeQuery(sqlUserId);
            rs.next();
            int userId = rs.getInt("user_id");

            //将评论写入数据库
            String sqlComment = "insert into weibo.comment values(" + commentNum + "," + weiboId + "," + userId + ",'" + commentContent + "')";
            stmt.execute(sqlComment);
            response.sendRedirect("comment_attention_user_weibo_success.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
