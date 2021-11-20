package app;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

@Slf4j
public class Mail {
    private static final Properties prop = new Properties();
    static {
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 587);
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.starttls.required", true);
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }

    public static void send(String username, String password, String list, String title, String letter) throws Exception {
        var session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        var message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        for (String address : list.split("\n")) {
            message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(address));
        }
        message.setSubject(title);

        var bodyPart = new MimeBodyPart();
        bodyPart.setContent(letter, "text/html");

        var multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        message.setContent(multipart);
        Transport.send(message);
    }
}
