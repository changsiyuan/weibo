package zieckey.login.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
@WebServlet(name = "MostPopularWeiboServlet", urlPatterns = {"/MostPopularWeiboServlet"})
public class MostPopularWeiboServlet extends HttpServlet {

    public MostPopularWeiboServlet() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        ServletOutputStream out = response.getOutputStream();

        response.setContentType("text/html");
        String result = "";

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
            
            //查询评论数量前10名的微博及其评论
            String sqlMostPopularWeibo = "select m.user_name, m.weibo_content, m.weibo_time, n.comment_content, m.comment_number from (select b.user_name, a.weibo_id, a.weibo_content, a.weibo_time, a.comment_number from weibo.weibo a join weibo.user b on a.user_id = b.user_id order by a.comment_number DESC LIMIT 10) m join weibo.comment n on m.weibo_id = n.weibo_id group by m.user_name, m.weibo_content, m.weibo_time, n.comment_content, m.comment_number order by m.comment_number DESC ";
            rs = stmt.executeQuery(sqlMostPopularWeibo);
            
            //将查询结果的行数查出来
            int popularWeiboNum = 0; 
            while (rs.next()) {
                popularWeiboNum++;
            }
            
            rs.beforeFirst();
            //将查询出的结果存入数组
            String[] userName = new String[popularWeiboNum];
            String[] weiboContent = new String[popularWeiboNum];
            String[] weiboTime = new String[popularWeiboNum];
            String [] commentContent = new String[popularWeiboNum];
            int [] commentNum = new int [popularWeiboNum];
            for (int i = 1, j = 0; i <= popularWeiboNum; i++, j++) {
                rs.next();
                userName[j] = rs.getString("user_name"); 
                weiboContent[j] = rs.getString("weibo_content");
                weiboTime[j] = rs.getString("weibo_time");
                commentContent[j] = rs.getString("comment_content");
                commentNum[j] = rs.getInt("comment_number");
            }
            
            //放入session中
            for (int j = 0, m = 1; j < popularWeiboNum; j++, m++) {
                String popularUserName = "pUserName" + m;
                String pwContent = "popularWeiboContent" + m;
                String pwTime = "popularWeiboTime" + m;
                String pComment = "popularComment" + m;
                String pContentNum = "pContentNum" + m;
                request.getSession().setAttribute(popularUserName, userName[j]);
                request.getSession().setAttribute(pwContent, weiboContent[j]);
                request.getSession().setAttribute(pwTime, weiboTime[j]);
                request.getSession().setAttribute(pComment, commentContent[j]);
                request.getSession().setAttribute(pContentNum, commentNum[j]);
            }
            
            //关注的用户的微博的数量传到前端
            request.getSession().setAttribute("popularWeiboNum", popularWeiboNum);
            
            response.sendRedirect("popular_weibo.jsp");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
