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
 * @author changsiyuan
 */
@WebServlet(name = "MyAttentionWeiboServlet", urlPatterns = {"/MyAttentionWeiboServlet"})
public class MyAttentionWeiboServlet extends HttpServlet {

    public MyAttentionWeiboServlet() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

        // 获取现在登陆的这个用户的用户名（目的是查询自己的微博）
        String UserName = session.getAttribute("UserName").toString();

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

            //查询现在登陆的用户的user_id
            String sqlLoginedUserId = "select user_id from weibo.user where user_name = '" + UserName + "'";
            rs = stmt.executeQuery(sqlLoginedUserId);
            rs.next();
            int loginedUserId = rs.getInt("user_id");

            //查询我关注的用户的微博和发微博的时间
            String sqlMyAttentionWeibo = "select n.user_name, m.weibo_content, m.weibo_id, m.weibo_time from (select a.attention_user_id, c.weibo_content, c.weibo_id, c.weibo_time from (select attention_user_id from weibo.attention where user_id = " + loginedUserId + ") a join weibo.weibo c on a.attention_user_id = c.user_id) m join weibo.user n on m.attention_user_id = n.user_id";
            rs = stmt.executeQuery(sqlMyAttentionWeibo);
            int attentionWeiboNum = 0; //记录现在登陆的这个用户关注的用户的微博的数量
            while (rs.next()) {
                attentionWeiboNum++;
            }

            rs.beforeFirst();
            //定义list存储关注的用户的微博的信息
            ArrayList<String[]> aUserWeibo = new ArrayList<String[]>();
            for (int i = 1; i <= attentionWeiboNum; i++) {
                rs.next();
                String[] attentionUserWeibo = new String[4];
                attentionUserWeibo[0] = rs.getString("user_name");
                attentionUserWeibo[1] = rs.getString("weibo_content");
                attentionUserWeibo[2] = Integer.toString(rs.getInt("weibo_id"));
                attentionUserWeibo[3] = rs.getString("weibo_time");
                aUserWeibo.add(attentionUserWeibo);
            }
            
            //将上面的list放入session
            request.getSession().setAttribute("attentionUserWeibo", aUserWeibo);

            //关注的用户的微博的数量传到前端
            request.getSession().setAttribute("attentionWeiboNum", attentionWeiboNum);

            response.sendRedirect("my_attention_weibo.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
