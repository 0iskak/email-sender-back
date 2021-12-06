# email-sender-back
## About
Web api for bulk mailing. \
See [commands](#commands).
### Built With JDK 17 and
* [Gradle](https://gradle.org/)
* [Spark](https://sparkjava.com/)
* [Lombok](https://projectlombok.org/)
* [JDBC](https://dev.mysql.com/downloads/connector/j/)
## Geting Started
1. Clone the repo 
    ```
    git clone https://github.com/0iskak/email-sender-back.git
    ```
2. Change variables
    1.  Change port in Main.java:
        ```
        port(Integer.parseInt(System.getProperty("PORT")));
        ```
    2. Change url, username and password (Tested with MySQL 5.7) in db/Connector.java:
        ```
        var url = "jdbc:"+System.getenv("JAWSDB_URL");
        var user = System.getenv("JAWSDB_USERNAME");
        var pwd = System.getenv("JAWSDB_PASSWORD");
        ```
3. Go to project's root directory and run by `.\gradlew run` \
or `.\gradlew shadowJar; java -jar build/libs/app-all.jar`
## Commands
Supports only Gmail SMTP. \
Pass your Gmail address as username and your Gmail password as password. \
If two factor enabled use [App Passwords](https://support.google.com/accounts/answer/185833), you can get it from [here](https://myaccount.google.com/apppasswords) 

All request methods should be POST. \
Returns true or requested data if no error. \
Returns error message if an error occurrs.
### API key management
`https://email-sender-back.herokuapp.com/api/create` - creates a new API key and returns value with a new API key \
`https://email-sender-back.herokuapp.com/api/renew/YOUR_API` - renews (renames) API key \
`https://email-sender-back.herokuapp.com/api/delete/YOUR_API` - deletes API key
### Email management
`https://email-sender-back.herokuapp.com/api/YOUR_API/add/EMAIL` - adds EMAIL address to the list \
`https://email-sender-back.herokuapp.com/api/YOUR_API/remove/EMAIL` - removes EMAIL address from the list \
`https://email-sender-back.herokuapp.com/api/YOUR_API/list` - returns the list of added email addresses
### Sender management
`https://email-sender-back.herokuapp.com/api/YOUR_API/mailer` - returns email address of sender \
`https://email-sender-back.herokuapp.com/api/YOUR_API/mailer/USERNAME/PASSWORD` - sets username and password for sender \
`https://email-sender-back.herokuapp.com/api/YOUR_API/send/SUBJECT/TEXT` - sends a message to all addresses from the list with SUBJECT as subject and TEXT as message.
