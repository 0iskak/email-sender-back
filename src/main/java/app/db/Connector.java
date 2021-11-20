package app.db;

import app.Mail;
import app.Password;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.UUID;

@Slf4j
public class Connector {
    private final Statement statement;

    @SneakyThrows
    public Connector() {
        var url = "jdbc:"+System.getenv("JAWSDB_URL");
        var user = System.getenv("JAWSDB_USERNAME");
        var pwd = System.getenv("JAWSDB_PASSWORD");
        Connection connection = DriverManager.getConnection(url, user, pwd);
        statement = connection.createStatement();
    }

    public Object create() {
        var api = UUID.randomUUID().toString();
        try {
            statement.execute("CREATE TABLE `"+api+"`(List VARCHAR(50) UNIQUE, Username VARCHAR(50), Password VARCHAR(50))");
            statement.execute("INSERT INTO `"+api+"` (`List`, `Username`, `Password`) VALUES (NULL, NULL, NULL)");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return api;
    }

    public Object delete(String api) {
        try {
            statement.execute("DROP TABLE `"+api+"`");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return true;
    }

    public Object renew(String api) {
        var newApi = UUID.randomUUID().toString();
        try {
            statement.execute("ALTER TABLE `"+api+"` RENAME `"+newApi+"`");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return newApi;
    }

    public Object add(String api, String email) {
        try {
            statement.execute("INSERT INTO `"+api+"` (`List`) VALUES ('"+email+"')");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return true;
    }

    public Object remove(String api, String email) {
        try {
            statement.execute("DELETE FROM `"+api+"` WHERE `List` LIKE '"+email+"'");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return true;
    }

    public Object list(String api) {
        var res = new StringBuilder();
        try {
            var set = statement.executeQuery("SELECT `List` FROM `"+api+"` WHERE `List` IS NOT NULL");
            while (set.next())
                res.append(set.getString(1)).append("\n");
        } catch (SQLException e) {
            return e.getMessage();
        }
        return res.toString();
    }

    public Object send(String api, String title, String letter) {
        try {
            var set = statement.executeQuery("SELECT `Password` FROM `"+api+"` WHERE `List` IS NULL");
            set.next();
            var password = Password.decrypt(set.getString(1));
            var username = mailer(api);
            var list = list(api);

            if (username instanceof String u && list instanceof String l) {
                Mail.send(u, password, l, title, letter);
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return true;
    }

    public Object mailer(String api) {
        var res = "";
        try {
            var set = statement.executeQuery("SELECT `Username` FROM `"+api+"` WHERE `List` IS NULL");
            set.next();
            res = set.getString(1);
        } catch (Exception e) {
            return e.getMessage();
        }
        return res;
    }


    public Object mailer(String api, String username, String password) {
        try {
            var encrypted = Password.encrypt(password);
            statement.execute("UPDATE `"+api+"` SET `Username` = '"+username+"', `Password` =  '"+encrypted+"' WHERE `List` IS NULL");
            return true;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
