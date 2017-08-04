package com.bell.arc.mail;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SecSendEmail {
	private String host;
	private int port;
	private String username;
	private String password;
	
	private final static String defaultSmtpServer = "smtp.gmail.com";
	private final static String defaultUsername = "stevexu01@gmail.com";
	private final static String defaultPassword = "password";
	private final static int smtpPortSSL = 465;
	private final static int smtpPortTLS = 587;
	
	private Properties props;
	
	private Session session;
	
	public enum  SecurityType {SSL, TLS;}
	
	/**
	 * Use this when only NOT using setPort(..)
	 */
	public void init(){
		init(SecurityType.SSL);
	}
	
	/**
	 * DO NOT use this when using setPort(..)
	 * 
	 * @param secType
	 */
	public void init(String secType){
		if(secType == null || secType.length() == 0){
			init(SecurityType.SSL);
		}else if(secType.equals("SSL")){
			init(SecurityType.SSL);
		}
		else if(secType.equals("TLS")){
			init(SecurityType.TLS);
		}
		
	}

	public void init(SecurityType secType){
		props = new Properties();
	    props.put("mail.smtp.host", (host == null || host.length() == 0)?defaultSmtpServer:host);
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.auth", "true");
	    props.setProperty("mail.smtp.socketFactory.fallback", "false");
	    
	    String prt = null;
	    switch (secType) {
		case SSL:
			 prt = (port == 0 ? smtpPortSSL  : port) + "";
			 props.put("mail.smtp.EnableSSL.enable", "true");		//for ssl
			 props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			break;
			
		case TLS:
			prt = (port == 0 ? smtpPortTLS  : port) + "";
			props.put("mail.smtp.starttls.enable", "true");	//for ttls
			break;

		default:
			prt = (port == 0 ? smtpPortSSL  : port) + "";
			 props.put("mail.smtp.EnableSSL.enable", "true");		//for ssl
			break;
		}
	    
	    props.setProperty("mail.smtp.port", prt);
		props.setProperty("mail.smtp.socketFactory.port", prt);
	    
	    setMailSession();

	}
	
	private void setMailSession(){
		final String u = (username == null || username.length() == 0)?defaultUsername:username;
		final String p = (password == null || password.length() == 0)?defaultPassword:password;
		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(u,p);
			}
		});
	}
	
	public void sendMessage(String subject, String body)  throws RuntimeException {
 
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@me.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("stevexu01@gmail.com"));
			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendMessage(String subject, String body, String filePath) throws RuntimeException {

		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@me.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("stevexu01@gmail.com"));
			message.setSubject(subject);
			

			Multipart multipart = new MimeMultipart();
			
			BodyPart messageBodyPart = new MimeBodyPart();
	        messageBodyPart.setText(body);
	        multipart.addBodyPart(messageBodyPart);
	        
	        messageBodyPart = new MimeBodyPart();
	        
	         DataSource source = new FileDataSource(filePath);
	         messageBodyPart.setDataHandler(new DataHandler(source));
	         messageBodyPart.setFileName(new File(filePath).getName());
	         multipart.addBodyPart(messageBodyPart);

	         message.setContent(multipart);
	         
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	private void send1()  throws RuntimeException{
		SecSendEmail mailSender = new SecSendEmail();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(465);
		mailSender.setUsername("stevexu@gmail.com");
		mailSender.setPassword("password");

		mailSender.init();
		
		mailSender.sendMessage("subject","body");
		//mailSender.sendMessage("subject","body","C:/_workspace/stockfirst/doc/howto.txt");
	}
	
	private void send2()  throws RuntimeException{
		SecSendEmail mailSender = new SecSendEmail();

		mailSender.init();
		
		mailSender.sendMessage("subject","body");
		//mailSender.sendMessage("subject","body","C:/_workspace/stockfirst/doc/howto.txt");
	}
	
	private void send3()  throws RuntimeException{
		SecSendEmail mailSender = new SecSendEmail();

		mailSender.init(SecurityType.SSL);
		
		mailSender.sendMessage("subject","body");
		//mailSender.sendMessage("subject","body","C:/_workspace/stockfirst/doc/howto.txt");
	}
	
	private void send4()  throws RuntimeException{
		SecSendEmail mailSender = new SecSendEmail();

		mailSender.init(SecurityType.TLS);
		
		//mailSender.sendMessage("subject","body");
		mailSender.sendMessage("subject","body","C:/_workspace/stockfirst/doc/howto.txt");
	}

	
}
