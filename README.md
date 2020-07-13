# Restaurant Management

CS 262 PROJECT

A database driven application,created for the lab course evaluation of the 4th semester.

## PROJECT INSTRUCTIONS

1. Download Apache Netbeans 11.3+ and MySQL Community Edition 8.0.19+..

2. Download MySQL and JDBC MySQL connector (if not already downloaded with netbeans)
which is compatible with your Netbeans version. ( use mysql-connector-java-8.0.19+ to ensure
integrity).

3. Download the project folder from here and place in any drive or documents folder.

4. Go to Netbeans -> Open Project -> Go to the project folder and open it. The project should be
opened in Netbeans. All required JAR files available under RMS → dist → lib.Add all JAR files
under given folder to libraries by right click LIBRARIES under RMS project and ADD JAR
FILES.If not available,Netbeans will prompt and you can resolve by downloading JARs online.

5. Change your MySQL user & password to ‘root’ & ‘root’ (which was our MySQL password) or
change the Connectivity.java file in the source code of RMS.rms project package. Change
‘root’ & ‘root’ your localhost:3306 username and password in both connectData() and
getConnection() under Connectivity.java

6. Go to MySQL and run the commands :
create database dominos;
use dominos;

7. Download the database folder and run all the .sql files on your database. Make sure all the
tables are correctly built inside your database.
8. Click on Run project button and choose rms.Welcome.java as main class or run
Welcome.java.(VERY IMPORTANT)

#### USAGE INSTRUCTIONS

1. Strictly Run Welcome.java after doing all steps and no error should be encountered.

2. FOR FAST LOGIN:

- Cashier Window:User=test Pass=test

- Admin Window:User=admin Pass=admin

3. Cashier Dashboard should after success cashier login and Admin Dashboard should open
after successful admin login.

4. Cashier can manage customers,view orders,and make orders.

5. Admin can manage Users,manage food items and view order statistics.

6. All menus and menu items are dynamically generated at run-time.

7. Message Dialog will prompt if you perform illegal actions on each step.
