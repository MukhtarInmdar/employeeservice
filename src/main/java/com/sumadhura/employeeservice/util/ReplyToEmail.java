package com.sumadhura.employeeservice.util;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder.FetchProfileItem;

public class ReplyToEmail {
	public static void main(String[] args) throws MessagingException {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		final String username = "teamamaravadhis@gmail.com";
		final String password = "zyhnroyfgrgxjszn";
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		boolean emailReceived = false;
		final String emailSubject = "Sumadhura Customer A1005 EOI for loan";
		Store store = null;
		try {
			store = session.getStore("imaps");
			store.connect("imap.gmail.com", username, password);
			
			// Create a Folder object and open the folder
			Folder folder = store.getFolder("inbox");
			folder.open(Folder.READ_WRITE);

			Message[] messages = folder.getMessages(1, folder.getMessages().length);
			
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfileItem.FLAGS);
			fp.add(FetchProfileItem.CONTENT_INFO);

			fp.add("X-mailer");
			folder.fetch(folder.getMessages(), fp); // Load the profile of the messages in 1 fetch.
			
			/*SearchTerm searchTerm = new AndTerm(new SubjectTerm(emailSubject),null);//SearchTerm searchTerm = new AndTerm(new SubjectTerm(emailSubject), new BodyTerm(emailSubject));
			Message[] messages = folder.search(searchTerm);
			for (Message message : messages) {
				if (message.getSubject().contains(emailSubject)) {
					System.out.println("Found the Email with Subject : " + emailSubject);
					emailReceived = true;
					break;
				}
			}*/

			//messages = folder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				   Message emailMessage = messages[i];
				   System.out.println();  
				   System.out.println("Email " + (i+1) + " -");  
				   System.out.println("Subject - " + emailMessage.getSubject());  
				   /*System.out.println("From - " + emailMessage.getFrom()[0]);
				   Date sent = emailMessage.getSentDate();
				   System.out.println("Sent: " + sent);*/
			}
			System.out.println("messages: " + Arrays.toString(messages));
			Message message = folder.getMessage(411);

			// Get all the information from the message
			String from = InternetAddress.toString(message.getFrom());
			if (from != null) {
				System.out.println("From: " + from);
			}
			String replyTo = InternetAddress.toString(message.getReplyTo());
			if (replyTo != null) {
				System.out.println("Reply-to: " + replyTo);
			}
			String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
			if (to != null) {
				System.out.println("To: " + to);
			}

			String subject = message.getSubject();
			if (subject != null) {
				System.out.println("Subject: " + subject);
			}
			Date sent = message.getSentDate();
			if (sent != null) {
				System.out.println("Sent: " + sent);
			}
			System.out.println(message.getContent());

			// compose the message to forward
			Message message2 = new MimeMessage(session);
			//message2 = (MimeMessage) message.reply(false);
			message2.setSubject("RE: " + message.getSubject());
			message2.setFrom(new InternetAddress(from));
			message2.setReplyTo(message.getReplyTo());

			message2.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Create your new message part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText("Oiginal message:nn");

			// Create a multi-part to combine the parts
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Create and fill part for the forwarded content
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(message2.getDataHandler());

			// Add part to multi part
			multipart.addBodyPart(messageBodyPart);

			// Associate multi-part with message
			message2.setContent(multipart);
			message2.setText("Thank you for ordering!");
			// Send message
			Transport.send(message2);

			System.out.println("message replied successfully ....");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	        if (store != null) {
	            store.close();
	        }
	    }
	}
}