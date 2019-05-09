# SpringBoot集成支付宝、微信支付sdk、实现动态配置quartz任务
<br><br>
## 一.部署项目<br>
1.请自行百度安装`idea`、`redis`、`mysql`、`dubbo`、`zookeeper`，这些是项目运行的基础支撑。<br>
2.项目导入idea，修改`application.yml`和`dubbo.properties`配置信息，改成自己本地的。<br>
3.运行项目下的`quartz.sql`文件，数据库名称随意叫，但是记得保持和`application.yml`mysql配置保持一致。<br>
4.如果不想装`dubbo`、`zookeeper`、`redis`这些，可以直接将相关代码注释掉或者直接干掉，运行main入口程序启动。<br>
5.项目遇到任何问题，启动不了的同学请联系我，我会提供相应支持。<br>
6.如果quartz任务不执行，看看quartz所属的任务组是不是启动了！！！特别注意，必须启动任务组，quartz才能启动！！！<br>
7.访问路径<br>
  任务组列表:http://127.0.0.1:8080/group<br>
  任务列表:http://127.0.0.1:8080/quartz<br>
  swagger:http://127.0.0.1:8080/swagger-ui.html
## 二.效果截图<br>
![](http://source.doudoucat.com/任务组.png)
![](http://source.doudoucat.com/任务列表.png)
![](http://source.doudoucat.com/quartz1.png)
<br>
## 三.说明与结语<br>
springboot动态配置quartz的demo，实现任务智能化便捷化执行。本项目可直接当做前后端分离的脚手架，除此以外，项目继承了微信、支付宝支付感兴趣的同学可以看看看<br>
有问题或者有任何疑问请加QQ群：259217951  包括你运行项目的问题，各种问题。我会一一回复你们。如果觉得代码生成器好用，请不要吝啬你的小星星，帮我点点star  thanks~~ 那将会是支持我前进的源动力！<br>
特别鸣谢SoJson群提供的平台，另外推荐一个特别好用的Json工具解析站，https://www.sojson.com
