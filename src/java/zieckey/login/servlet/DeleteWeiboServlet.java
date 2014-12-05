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
@WebServlet(name = "DeleteWeiboServlet", urlPatterns = {"/DeleteWeiboServlet"})
public class DeleteWeiboServlet extends HttpServlet {

    public DeleteWeiboServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

        // 获取现在登陆的这个用户的用户名（目的是查询自己的微博）
        String sUserName = session.getAttribute("UserName").toString();

        //获取需要删除的微博的id
        int weiboDelete = Integer.parseInt(request.getParameter("weiboId"));

        //获取需要删除的微博的在数据库表中的weibo_id
        ArrayList<String[]> myWeiboList = new ArrayList<String[]>();
        myWeiboList = (ArrayList) session.getAttribute("myWeiboList");
        int deleteWeiboId = Integer.parseInt(myWeiboList.get(weiboDelete)[0]);

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
            String sqlDeleteWeibo = "delete from weibo.weibo where weibo_id = " + deleteWeiboId;
            stmt.execute(sqlDeleteWeibo);
            
            //跳转到删除后的提示页面
            response.sendRedirect("my_weibo_has_delete.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
