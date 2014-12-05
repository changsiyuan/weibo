/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zieckey.login.servlet;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static javax.swing.UIManager.getInt;

/**
 *
 * @author lenovo
 */
public class RegisterServlet extends HttpServlet implements Servlet {

    public RegisterServlet() {

    }

    @Override
    protected void doGet(HttpServletRequest arg0, HttpServletResponse arg1)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String result = "";

        // 获取用户名
        String sUserName = request.getParameter("txtUserName");
        if (sUserName == "" || sUserName == null || sUserName.length() > 20) {
            try {
                result = "请输入用户名（不超过20字符）！";
                request.setAttribute("ErrorUserName", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }

        // 获取密码
        String sPasswd = request.getParameter("txtPassword");
        if (sPasswd == "" || sPasswd == null || sPasswd.length() > 20) {
            try {
                result = "请输入密码（不超过20字符）！";
                request.setAttribute("ErrorPassword", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }

        // 获取电子邮箱
        String sUserEmail = request.getParameter("txtEmail");
        if (sUserEmail == "" || sUserEmail == null || sUserEmail.length() > 50) {
            try {
                result = "请输入邮箱（不超过50字符）！";
                request.setAttribute("ErrorEmail", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }
        // 获取用户个人信息
        String sUserDescription = request.getParameter("txtDescription");
        if (sUserDescription == "" || sUserDescription == null || sUserDescription.length() > 100) {
            try {
                result = "请输入用户个人描述（不超过100字符）！";
                request.setAttribute("ErrorDescription", result);
                response.sendRedirect("register.html");
            } catch (Exception e) {
            }
        }

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
            stmt = connection.createStatement();
            // SQL语句
            String sqlCheckUser = "select * from weibo.user where user_name = '" + sUserName + "'";
            rs = stmt.executeQuery(sqlCheckUser);// 返回查询结果
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            if (rs.next())// 如果记录集非空，此用户名已经存在
            {
                response.sendRedirect("register_failure.jsp");
            } else {
                //否则将用户名密码等信息记录到数据库中
                String largestId = "select user_id from weibo.user order by user_id DESC LIMIT 1";
                rs = stmt.executeQuery(largestId);
                rs.next();
                int i = rs.getInt("user_id"); //取出数据库中目前的最大的user_id
                i++;
                System.out.println(i);
                String sqlAddUser = "insert into weibo.user values(" + i + ",'" + sUserName + "','" + sUserEmail + "','" + sUserDescription + "','" + sPasswd + "')";
                stmt.execute(sqlAddUser);  //只执行sql语句，不返回结果

                //转换成session变量，方便在注册成功的页面显示
                request.getSession().setAttribute("UserName", sUserName);
                request.getSession().setAttribute("UserEmail", sUserEmail);
                request.getSession().setAttribute("UserDescription", sUserDescription);

                response.sendRedirect("register_success.jsp");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
    }
}
