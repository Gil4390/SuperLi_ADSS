package BusinessLayer.Generators;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailGenerator {
    private static class EmailGeneratorHolder{
        private final static EmailGenerator instance = new EmailGenerator();
    }

    public static EmailGenerator getInstance() {
        return EmailGeneratorHolder.instance;
    }
    private EmailGenerator() {
    }
    public Session getSession() {
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("superlimanagement@gmail.com", "etwbblhvgoijysst");
            }
        });
        session.setDebug(false);
        return session;
    }

    public void sendOrderEmail(String email, String pdfPath) {
        try {
            Session session = getSession();
            MimeMessage message = new MimeMessage(session);
            String from = "SuperLiManagement@gmail.com";
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Order from superLi");
            message.setText("Hello there is a new order from superLi");
            DataSource source = new FileDataSource(pdfPath);
            message.setDataHandler(new DataHandler(source));
            message.setFileName("SuperLiOrder.pdf");
            Transport.send(message);
        }
        catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
