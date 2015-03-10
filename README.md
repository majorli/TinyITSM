# TinyITSM
assistant application for IT services

IT service employees can use this application to assist their daily work.<br>
The application provides working assistance of IT projects construction, IT system maintenance, IT service providing, and IT assets management.<br>
It also provides a tiny IT knowledgebase to store and get documents.

# About deployment

1. Environment<br>
  1.1 Server - Apache Tomcat 8<br>
    1.1.1 Server.xml<br>
    ```
      ...
      <!-- define the global datasource in <GlobalNamingResources> section -->
    	<GlobalNamingResources>
        ...
    		<!-- 全局数据源，使用者：TinyITSM、TinyConnector，连接MySQL5.6数据库tinyitsm -->
    		<Resource 
    			name="jdbc/tinyitsm"
    			auth="Container"
    			type="javax.sql.DataSource"
    			factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
    			testWhileIdle="true"
    			testOnBorrow="true"
    			testOnReturn="false"
    			validationQuery="SELECT 1"
    			validationInterval="30000"
    			timeBetweenEvictionRunsMillis="30000"
    			maxActive="100"
    			minIdle="10"
    			maxWait="10000"
    			initialSize="10"
    			removeAbandonedTimeout="60"
    			removeAbandoned="true"
    			logAbandoned="true"
    			minEvictableIdleTimeMillis="30000"
    			jmxEnabled="true"
    			jdbcInterceptors="org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;
    			  org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer"
    			username="[db_user_username]"
    			password="[db_user_password]"
    			driverClassName="com.mysql.jdbc.Driver"
    			url="jdbc:mysql://localhost/tinyitsm?autoReconnect=true&amp;failOverReadOnly=false" />
    		
    	</GlobalNamingResources>
      ...
      <!-- define applications in <Service><Engine><Host> section -->
      ...
      			<Host appBase="webapps" autoDeploy="true" name="localhost" unpackWARs="true">
              ...
    				  <!-- 使用全局数据源连接数据库，不在应用上下文中单独设置
              <Context docBase="{path_to}/itsm" path="/itsm" reloadable="true"/>
    				  <Context docBase="{path_to}/tinyConn" path="/tinyconn" reloadable="true"/>
    			  </Host>
      ...
    ```
    1.1.2 Context.xml<br>
    ```
    ...
    <Context>
      ...
      <!-- 对全局jndi数据源tinyitsm的全局链接 -->
      <ResourceLink global="jdbc/tinyitsm" name="jdbc/tinyitsm" type="javax.sql.DataSource" />
    </Context>
    ```
    1.1.3 web.xml (of tomcat)<br>
    ```
      ...
      <servlet>
        <servlet-name>default</servlet-name>
        <servlet-class>org.apache.catalina.servlets.DefaultServlet</servlet-class>
        <init-param>
            <param-name>debug</param-name>
            <param-value>0</param-value>
        </init-param>
        <!-- make sure this parameter is set to false -->
        <init-param>
            <param-name>listings</param-name>
            <param-value>false</param-value>
        </init-param>
        <!-- add this parameter to enable http methods PUT and DELETE -->
        <init-param>
        	<param-name>readonly</param-name>
        	<param-value>false</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
      </servlet>
      ...
    ```
  1.2 Database - mysql5.6<br>
  It's recommended that remain the Hibernate property "hibernate.hbm2ddl.auto" with value of "auto", so that Hibernate will create all of the tables needed when you launche tomcat the first time after the applications is deployed. After that, delete or comment off this property immediatly, import initial data using "companies.sql", then reload application.
<br>
3. Struts2+Hibernate4+Spring4<br>
3. Log4j2<br>
