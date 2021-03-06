# improvement of this program


## sql注入风险问题。

如果用户名输入  

```
' or 1=1 limit 1  --
```

此时在loginservlet中会执行拼接成的完整的sql语句：
  
```
select * from weibo.user where user_name='' or 1=1 limit 1 --' and user_passwd = 'a'
```
  
即密码部分被注释掉，可以用一个不存在的用户名实现登录。
这说明本程序的servlet中广泛使用的sql拼接有较大的被sql注入攻击的风险。


## 跳转页面开销大问题。

servlet中频繁的使用response.sendRedirect("login_success.jsp");调转界面，会增加网络上的开销，因为这个URL要被传输到客户端，客户端再次执行新的请求。   
完全可以在server端直接从一个servlet跳转到另一个servlet，不用再传回客户端数据，减少网络开销。
我在SearchUserServlet中的post方法做了改进：直接用forward跳转到另一个servlet或jsp；

```
  //跳转到login_success.jsp页面显示所搜结果
  //这里注意，不要用response跳转，因为这样服务器端将URL发回客户端，客户端重新请求，网络开销大。
  //应当用下面的RequestDispatcher直接在服务器端跳转到相应的jsp显示即可，此时虽然跳转到jsp，但是URL仍显示servlet。
    RequestDispatcher rd = request.getRequestDispatcher("user_search.jsp");
    rd.forward(request, response);
            
  //  response.sendRedirect("user_search.jsp");
```

## 页面模块化问题。

可以用两个include使得页面模块化，即页面的不同部分对应不同的servlet或jsp：

```
  RequestDispatcher rd = request.getRequestDispatcher("user_search.jsp");
  rd.include(request, response);

  RequestDispatcher rd1 = request.getRequestDispatcher("test.jsp");
  rd1.include(request, response);
```

注意：用include后不能再用forword方法；

## 创建weibo类问题。

原来的做法是将weibo的属性放在一个数组中，将若干个数组放到weiboList中。
现在则将每条微博的所有属性放在一个weibo类中，将若干个微博类实例化后压入list，再将list放入session中，jsp从session中取出list，再从list中取出某个weibo类，然后再取出某条微博的某个属性。

专门建一个weibo.java，定义每条微博的内容，并自动生成get和set方法：
  
```
public class Weibo {

    private long weibo_id;
    private long user_id;
    private String weibo_content;
    private Date weibo_time;
    private long comment_number;

    public long getWeibo_id() {
        return weibo_id;
    }

    public void setWeibo_id(long weibo_id) {
        this.weibo_id = weibo_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getWeibo_content() {
        return weibo_content;
    }

    public void setWeibo_content(String weibo_content) {
        this.weibo_content = weibo_content;
    }

    public Date getWeibo_time() {
        return weibo_time;
    }

    public void setWeibo_time(Date weibo_time) {
        this.weibo_time = weibo_time;
    }

    public long getComment_number() {
        return comment_number;
    }

    public void setComment_number(long comment_number) {
        this.comment_number = comment_number;
    }
}
```

在servlet中，不再将微博的属性放到数组中，而是利用weibo对象的set方法：

```
  ArrayList<Weibo> weibo = new ArrayList<Weibo>();

  Weibo w = new Weibo();
  w.setWeibo_id(rs.getLong("weibo_id"));
  w.setUser_id(rs.getLong("user_id"));
  w.setWeibo_content(rs.getString("weibo_content"));
  w.setWeibo_time(rs.getDate("weibo_time"));
  w.setComment_number(rs.getLong("comment_number"));

  weibo.add(w);
  request.getSession().setAttribute("myWeiboList", weibo);
```
  
然后依次将每个微博对象压入list中。然后再将list放到session中。
jsp从session中取出list（即若干个微博对象），然后通过如下方法显示：

```
  <%
       for (int i = 0; i < myWeiboNum; i++) {
  %>
  <%=myWeibo.get(i).getWeibo_content()%>
   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <%=myWeibo.get(i).getWeibo_time()%>
```

## 某些sql语句过长问题。

筛选“我关注的人的微博的评论”的sql语句过长。原来的语句是这样写的：先从微博表和关注表中筛选，再将筛选结果当做一个新表，从这个新表和评论表中筛选，这样效率太低。
新的sql语句如下，直接从三个表中筛选，结果按照微博内容列出：
  
```
select b.user_id, b.weibo_content, b.weibo_time, c.user_id, c.comment_content
from weibo.attention a join weibo.weibo b 

on a.attention_user_id = b.user_id 

join weibo.comment c
on b.weibo_id = c.weibo_id

where a.user_id = 5859 

order by b.weibo_content
```
