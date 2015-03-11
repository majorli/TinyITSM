# TinyITSM
***Assistant application for IT services***

IT service employees can use this application to assist their daily work.<br>
The application provides working assistance of IT projects construction, IT system maintenance, IT service providing, and IT assets management.<br>
It also provides a tiny IT knowledgebase to store and get documents.<br>

## About deployment

### Server
- We use **Apache Tomcat 8.0.14** as our server.
- Add a global naming resource named **jdbc/tinyitsm** to the *GlobalNamingResources* element in *server.xml* of tomcat to define our data source. Add two webapp contexts of **TinyITSM** and **TinyConn** to the *Host* element in *server.xml* of tomcat to define our two applications. See file ***deployment/tomcat/conf/server.xml***.
- Add a resource link named **jdbc/tinyitsm** to the *ResourceLink* element in *context.xml* of tomcat to define a global link point to our datasource. See file ***deployment/tomcat/conf/context.xml***.
- Add parameter **readonly** to the *default* servlet in *web.xml* of tomcat to enable HTTP methods PUT and DELETE. See file ***deployment/tomcat/conf/web.xml***.

### Database
- We use **MySQL 5.6** as our database.
- It's recommended to remain the Hibernate property "hibernate.hbm2ddl.auto" with value "auto", so that Hibernate will create all of the tables needed when you launche tomcat the first time. After that, delete or comment off this property immediately, import initial data using ***deployment/database/companies.sql***, then reload application.
- **Note:** MySQL connector/J package (***mysql-connector-java-5.1.34.jar***) must be put into ***{TOMCAT_HOME}/lib/***.

### Log4j 2
- We use **log4j 2** as our logging tool.
- Configuration file ***log4j2.xml*** for linux server is put in ***deployment/linux/src/main/resources*** folder. You should use this file on the server.

### Struts 2
- **Struts 2.3.16.3** is our web framework.
- Configuration file ***strust.xml*** for linux server is put in ***deployment/linux/src/main/resources*** folder. You should use this file on the server.

### Hibernate 4
- **Hibernate 4.3.7.Final** is our ORM framework.
- Hibernate is integrated with Spring framework, so all configurations of Hibernate are located in ***spring.xml***.
- Configuration file ***spring.xml*** for linux server is put in ***deployment/linux/src/main/resources*** folder. You should use this file on the server.

### Spring 4
- **Spring 4.1.3.RELEASE** is our DI framework.
- Configuration file ***spring.xml*** for linux server is put in ***deployment/linux/src/main/resources*** folder. You should use this file on the server.

### Hibernate Search 5 and Lucene
- We use **Hibernate Search 5.0.1.Final** with **Apache Lucene** as our fulltext search framework.
- As of Hibernate ORM, all configurations of Hibernate Search are located in ***spring.xml***.

### Depolyment notes
- First, install **Java 7**, **Apache Tomcat 8.0.14**, **MySQL 5.6 server** on the server and config them correctly.
- Deploy **TinyITSM** and **TinyConn** on tomcat server, using config files in ***deployment/linux*** and ***deployment/tomcat*** folders.
- Create a folder in a large volumn and create a soft link point to it in ***{TinyITSM_context_root}*** folder named **cloud**.
- Launch tomcat for the first time.
- Import initial data from ***deployment/companies.sql***.
- Modify ***{TinyITSM_context_root}/WEB-INF/classes/spring.xml*** file, delete or comment off the Hibernate property **hibernate.hbm2ddl.auto**.
- Reload **TinyITSM** or relaunch the tomcat server.
