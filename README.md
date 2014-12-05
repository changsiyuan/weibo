weibo
=====
# 本程序的说明

## 本程序的功能
- 本程序可以实现基本的微博功能，具体包括：

    用户可以注册、登录、退出，注册时保证用户名唯一；

    登录后可以搜索其他用户，查看用户详情，可以关注、取消关注；
    
    可以查看我关注的人及他们的微博和每条微博的评论，关注我的人；

    可以发布微博，仅支持文本消息；可以删除微博；
    
    可以对一个自己已经关注的用户的微博进行评论、删除评论；

    可以查看我发的微博，查看我关注的微博，查看最热微博的列表（最热微博是指评论数前十的微博）；
    
## 本程序前端到后端流程
- html静态页面
- jsp动态页面，掺杂java帮助页面显示
- servlet接收jsp的数据和请求并在database中查询所需数据
- database维护四张数据表，每个表有若干字段，分别存储用户信息、微博信息、评论信息、关注信息，数据库表中的数据随机生成，具体生成的程序见[链接](https://github.com/changsiyuan/creat_weibo_data)

## 本程序的亮点
- servlet和jsp通信是通过session，servlet将查询数据库表的结果放在session中，jsp从session取出想要的数据，并在前端显示。
- 删除微博功能的设计思路如下：将每条微博的“删除”按钮赋予一个id（在显示微博的时候顺便通过for循环赋予），而这个id正好是这条微博在list中的偏移量，这样就将微博在list偏移量的信息传递给servlet，从而使得servlet找到这条微博在数据库表中的id，实现删除。

## 本程序用到的较为复杂的sql查询语句
- 查询关注我的人的微博和时间
'''
select n.user_name, m.weibo_content, m.weibo_id, m.weibo_time

from (select a.attention_user_id, c.weibo_content, c.weibo_id, c.weibo_time 
from (select attention_user_id from weibo.attention where user_id = 2525) a join weibo.weibo c
on a.attention_user_id = c.user_id) m join weibo.user n

on m.attention_user_id = n.user_id
'''
- 查询最热微博及评论
'''
select m.user_name, m.weibo_content, m.weibo_time, n.comment_content, m.comment_number


from (select b.user_name, a.weibo_id, a.weibo_content, a.weibo_time, a.comment_number
from weibo.weibo a join weibo.user b
on a.user_id = b.user_id
order by a.comment_number DESC LIMIT 10) m join weibo.comment n

on m.weibo_id = n.weibo_id
group by m.user_name, m.weibo_content, m.weibo_time, n.comment_content, m.comment_number
order by m.comment_number DESC 
'''

