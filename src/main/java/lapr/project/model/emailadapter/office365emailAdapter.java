package lapr.project.model.emailAdapter;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import lapr.project.model.ServiceEmail;

/**
 * EmailAdapter to establish the connection with the office365 email service
 * Adapted from: https://gist.github.com/brunocesarsilva/12a529f7f752f2853b9f
 */
public class office365emailAdapter implements ServiceEmail {

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    private static final String SMTP_SERVER = "smtp.office365.com";
    private static final int SMTP_SERVER_PORT = 587;
    private static final String EMAIL_ACCOUNT = "lapr3g19@outlook.pt";
    private static final String CHAVE = "g19123456";

    /**
     * Sends the email through the defined SMTP_SERVER
     * @param destinationEmail String containing the destination email address
     * @param subject subject line of the email
     * @param emailContent String content of the email
     * @return boolean true if sent correctly
     *         false if not sent
     */
    @Override
    public boolean sendEmail(String destinationEmail, String subject,  String emailContent) {

        final Session session = Session.getInstance(this.getEmailProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_ACCOUNT, CHAVE);
            }
        });

        try{
            final Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));
            message.setFrom(new InternetAddress(EMAIL_ACCOUNT));
            message.setSubject(subject);
            message.setText(emailContent);
            message.setSentDate(new Date());
            //Transport.send(message); todo uncomment
            return true;
        } catch (final MessagingException ex) {
            LOGGER.log(Level.WARNING, "Error sending email: " + ex.getMessage(), ex);
            return false;
        }
        //return true;
    }

    /**
     * Sets necessary configurations to access the server and returns them in a Properties object
     * @return Properties object with the defined configurations
     */
    private Properties getEmailProperties() {
        final Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", SMTP_SERVER);
        config.put("mail.smtp.port", SMTP_SERVER_PORT);
        return config;
    }

}
