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
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author lenovo
 */
@WebServlet(name = "CommentAttentionWeiboServlet", urlPatterns = {"/CommentAttentionWeiboServlet"})
public class CommentAttentionWeiboServlet extends HttpServlet {

    public CommentAttentionWeiboServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

        //获取需要查看评论的微博在数据库表中的weibo_id
        int weiboComment = Integer.parseInt(request.getParameter("weiboID"));

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

            String sqlAttentionUserWeiboComment = "select comment_id, user_name, comment_content from(select comment_id, user_id, comment_content from weibo.comment where weibo_id = " + weiboComment + ") a join weibo.user b on a.user_id = b.user_id";
            rs = stmt.executeQuery(sqlAttentionUserWeiboComment);

            //查询这条微博的评论数
            int commentNum = 0;
            while (rs.next()) {
                commentNum++;
            }

            //将评论及相关信息放在list中
            rs.beforeFirst();
            ArrayList<String[]> attentionUserWeiboComment = new ArrayList<String[]>();
            for (int i = 1; i <= commentNum; i++) {
                rs.next();
                String[] comment = new String[3];
                comment[0] = Integer.toString(rs.getInt("comment_id"));
                comment[1] = rs.getString("user_name");
                comment[2] = rs.getString("comment_content");
                attentionUserWeiboComment.add(comment);
            }

            //将上面的list放入session
            request.getSession().setAttribute("attentionUserWeiboComment", attentionUserWeiboComment);

            //将评论总数放入session
            request.getSession().setAttribute("commentNum", commentNum);
            
            //将微博的id放入session
            request.getSession().setAttribute("commentWeiboId",weiboComment);

            response.sendRedirect("attentionUser_weibo_comment.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
