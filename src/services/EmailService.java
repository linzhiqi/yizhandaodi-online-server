package services;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class EmailService {
	
	

	public static boolean sendNewPassword(String address, String password){
		final String username = "techzhiqi@gmail.com";
		final String emailPassword = "Gresdfiqp7Yen0Tree-Growing7High";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, emailPassword);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("techzhiqi@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(address));
			message.setSubject("站神秘籍密码重置");
			message.setText("新密码: "
				+ password + "\n\n该密码为随机生成，请使用该密码在手机端登陆后，进入修改密码页面重新设置密码");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
