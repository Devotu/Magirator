package magirator.support;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mail {

	public static void SendMail(String to, String subject, String content) throws AddressException, MessagingException {

		// Sender's email ID needs to be mentioned
		// String from = "support@magirator.net";
		String from = "Support";

		// Assuming you are sending email from localhost
		String host = "localhost";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);

		// Create a default MimeMessage object.
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));

		// Set To: header field of the header.
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// Set Subject: header field
		message.setSubject(subject);

		// Now set the actual message
		message.setText(content);

		// Send message
		Transport.send(message);

	}

}
