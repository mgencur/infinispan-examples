How to run the example
======================

CarMart is a simple web application that uses Infinispan which is backed by a relational 
database (locally installed MySQL DB).
Users can list cars, add new cars or remove them from the CarMart. Information about each car
is stored in a cache. The application also shows cache statistics like stores, hits, retrievals, etc.

Caches are configured programatically and run in the same JVM as the web application.


Building and deploying to JBoss AS 7
------------------------------------

0) Prepare local MySQL database:

    `carmartdb`
    
   acessible with the following credentials:
   
    `username: carmart`
    `password: carmart`

1) Adjust datasources configuration in the configuration file for JBossAS ($JBOSS_HOME/standalone/configuration/standalone.xml):

    `<subsystem xmlns="urn:jboss:domain:datasources:1.0">
        <datasources>
            <datasource jndi-name="java:jboss/datasources/ExampleDS" pool-name="ExampleDS" enabled="true" use-java-context="true">
                <connection-url>jdbc:mysql://localhost:3306/carmartdb</connection-url>
                <driver>mysql-connector-java-5.1.17-bin.jar</driver>
                <security>
                    <user-name>carmart</user-name>
                    <password>carmart</password>
                </security>
            </datasource>
        </datasources>
    </subsystem>`

2) Copy Jdbc driver jar file for MySQL DB into $JBOSS_HOME/standalone/deployments, 
   e.g. mysql-connector-java-5.1.17-bin.jar

3) Start JBoss AS 7 where your application will run

    `$JBOSS_HOME/bin/standalone.sh`

4) Build and deploy the application

    `mvn clean package jboss-as:deploy -Pjbossas`

5) Go to http://localhost:8080/carmart-tx-jdbc

6) Undeploy the application

    `mvn jboss-as:undeploy -Pjbossas`


Building and deploying to Tomcat 7
------------------------------------

0) Prepare local MySQL database:

    `carmartdb`
    
   acessible with the following credentials:
   
    `username: carmart`
    `password: carmart`

1) Copy Jdbc driver jar file for MySQL DB into $TOMCAT_HOME/lib, 
   e.g. mysql-connector-java-5.1.17-bin.jar

2) Start Tomcat 7 where your application will run

    `$TOMCAT_HOME/bin/catalina.sh start`

3) Build and deploy the application

    `mvn clean package tomcat:deploy -Ptomcat`

NOTE: You need to add the following snippet to your configuration file for Maven, i.e. ~/.m2/settings.xml:

    `<server>
         <id>tomcat</id>
         <username>admin</username>
         <password>admin</password>
    </server>`
    
And configure Tomcat in the same way - add user admin to $TOMCAT_HOME/conf/tomcat-users.xml:
    
    `<role rolename="manager-script"/>
     <user username="admin" password="admin" roles="manager-script"/>`
     
After that, Maven will be able to deploy the application to the running Tomcat container.

4) Go to http://localhost:8080/carmart-tx-jdbc

5) Undeploy the application

    `mvn tomcat:undeploy -Ptomcat`


Building and deploying to JBoss AS 7 with C3P0 datasource
---------------------------------------------------------

In this profile we use PooledConnectionFactory and thus connect to relational database directly 
without managed datasource. As a results, we do not have to define datasources in standalone.xml nor
deploy jdbc driver as a standalone deployment but rather package it with application.

0) Prepare local MySQL database:

    `carmartdb`
    
   acessible with the following credentials:
   
    `username: carmart`
    `password: carmart`

1) Start JBoss AS 7 where your application will run

    `$JBOSS_HOME/bin/standalone.sh`

2) Build and deploy the application

    `mvn clean package jboss-as:deploy -Pjbossas-c3p0`

3) Go to http://localhost:8080/carmart-tx-jdbc

4) Undeploy the application

    `mvn jboss-as:undeploy -Pjbossas-c3p0`
