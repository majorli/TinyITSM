/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

LOCK TABLES `functions` WRITE;
/*!40000 ALTER TABLE `functions` DISABLE KEYS */;
INSERT INTO `functions` VALUES (1,'\0','ADMIN','','\0',99,'','系统管理'),(2,'','TINYHR','ADMIN','\0',1,'admin/tinyhr.jsp','TinyHR人力资源'),(3,'','SETTINGS','ADMIN','\0',2,'admin/settings.jsp','系统控制台'),(4,'\0','HOME_PAGE','UTIL','\0',0,'portal/home.jsp',''),(5,'\0','SERVICE','','\0',10,'','信息化服务'),(6,'\0','SVR_REQ','SERVICE','\0',1,'service/svr_req.jsp','服务申请'),(7,'\0','SVR_ACPT','SERVICE','',2,'service/svr_accept.jsp','服务受理'),(8,'\0','SVR_APPR','SERVICE','\0',3,'service/svr_approve.jsp','服务审批'),(9,'\0','SVR_EXEC','SERVICE','',5,'service/svr_exec.jsp','服务执行'),(10,'\0','SVR_LIST','PORTAL','\0',3,'portal/svr_list.jsp','信息化服务目录'),(11,'\0','SVR_PUB','SERVICE','',7,'service/svr_pub.jsp','服务发布'),(12,'\0','PORTAL','','\0',2,'','服务门户'),(13,'\0','AST_HOME','ASSET','',2,'asset/ast_home.jsp','资产管理'),(14,'\0','TASKS','PORTAL','\0',2,'portal/tasks.jsp','待办事项'),(15,'\0','USR_SET','UTIL','\0',0,'portal/user-setting.jsp',''),(16,'\0','ASSET','','\0',20,'','信息化资产'),(17,'\0','PROJECT','','\0',30,'','信息化项目'),(19,'\0','MAINTAIN','','\0',40,'','信息系统运维'),(20,'\0','CLOUD','','\0',50,'','信息化资料库'),(21,'\0','CLD_HOME','CLOUD','',1,'cloud/cloud_home.jsp','资料库首页'),(22,'\0','CLD_PRIV','CLOUD','',2,'cloud/cloud_private.jsp','我的资料库'),(23,'\0','CLD_VIEW','CLOUD','',3,'cloud/cloud_view.jsp','资料分类视图'),(24,'\0','AST_OWNED','ASSET','\0',1,'asset/ast_owned.jsp','我的资产'),(25,'\0','AST_NEW','UTIL','',0,'asset/ast_new.jsp','新增资产');
/*!40000 ALTER TABLE `functions` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
