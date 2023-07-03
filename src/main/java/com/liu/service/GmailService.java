package com.liu.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class GmailService {

	@Autowired
	private Gmail gService;
	
	private String myEmail = "lys7744110@gmail.com";

	public String getMyEmail() {
		return myEmail;
	}

	public MimeMessage createEmail(String toEmailAddress, String fromEmailAddress, String subject, String bodyText)
			throws AddressException, MessagingException, UnsupportedEncodingException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage email = new MimeMessage(session);

		email.setFrom(new InternetAddress(fromEmailAddress,"Carbon"));
		email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(toEmailAddress));
		email.setSubject(subject);
		email.setText(bodyText);
		return email;
	}

	public Message createMessageWithEmail(MimeMessage emailContent) throws IOException, MessagingException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		emailContent.writeTo(buffer);
		byte[] bytes = buffer.toByteArray();
		String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
		Message message = new Message();
		message.setRaw(encodedEmail);
		return message;
	}

	public Message sendMessage(String toEmailAddress, String fromEmailAddress, String subject, String bodyText)
			throws AddressException, MessagingException, IOException {
		MimeMessage email = createEmail(toEmailAddress, fromEmailAddress, subject, bodyText);
		Message message = createMessageWithEmail(email);

		try {
			message = gService.users().messages().send("me", message).execute();
			System.out.println("Message id: " + message.getId());
			System.out.println(message.toPrettyString());
			return message;
		} catch (GoogleJsonResponseException e) {
			GoogleJsonError error = e.getDetails();
			if (error.getCode() == 403) {
				System.err.println("Unable to send message: " + e.getDetails());
			} else {
				throw e;
			}

		}
		return null;
	}
}
