# 介绍 #
分布式网上商城

# 使用技术 #
- spring+mybatis+springmvc
- maven:项目管理,分模块开发
- easyui:后台页面搭建
- KindEditor:富文本编辑器
- redis:缓存商品信息与首页信息,记录用户购物车信息,sso系统存放token
- solr/solrcloud:商品检索系统
- FastDFS+nginx:图片存储
- activemq:商品系统,检索系统,后台系统之间异步通信
- freemarker:网页静态化
- dubbo:远程调用
- zookeeper:服务注册中心
- mysql
# 运行 #
- zookeeper+solrcloud的虚拟机一台,并运行
- redis集群+activemq的虚拟机一台,并运行
- 导入mysql数据
- 把源码导入ide,修改zookeeper,redis,solrserver,activemq的配置,即可运行​
