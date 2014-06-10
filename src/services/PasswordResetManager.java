package services;

import java.util.Random;

import org.apache.log4j.Logger;

import game.Account;

import protocol.ClientSendPasswordSignal;
import protocol.EmailValidator;
import protocol.ServerSendPasswordSignal;
import dao.AccountJDBC;

public class PasswordResetManager {
	static EmailValidator validator = new EmailValidator();
	private static final char[] symbols = new char[36];
	private final static int PASSWORD_LEN = 10;
	private static Logger logger = Logger.getLogger(PasswordResetManager.class);

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	public static ServerSendPasswordSignal serveRegistration(
			ClientSendPasswordSignal signal, AccountJDBC db, String remoteIp) {
		logger.info("got reset password for email:" + signal.email + " from ip:" +remoteIp);
		ServerSendPasswordSignal sSig = new ServerSendPasswordSignal();	
		if (!validator.isValide(signal.email)) {
			sSig.sentOK = false;
			sSig.emailValidated = false;
			logger.error("email:"+signal.email + "is invalide, from ip:" + remoteIp);
		}else{
			sSig.emailValidated = true;
			Account acct = null;
			db.connect();
			if ((acct = db.getAccountByEmail(signal.email)) != null) {
				sSig.emailRegistered = true;
				String oldPwd = acct.pwd;
				String newPwd = RandomString(PASSWORD_LEN);
				System.out.println("new password is genrated: " + newPwd);
				if(db.updatePasswordById(acct.id, newPwd)){
					if(sendEmail(signal.email,newPwd)){
						sSig.sentOK = true;
					}else{
						db.updatePasswordById(acct.id, oldPwd);
						sSig.sentOK = false;
						sSig.emailServiceFailed = true;
					}
				}
			} else {
				logger.error("email:"+signal.email + "doesn't exist, from ip:" + remoteIp);
				sSig.sentOK = false;
				sSig.emailRegistered = false;
			}
			db.finalize();
		}
		
		return sSig;
	}
	
	
	private static boolean sendEmail(String email, String newPwd) {
		return EmailService.sendNewPassword(email, newPwd);
	}


	private static String RandomString(int length)
	  {
		Random random = new Random();
	    char[]buf = new char[length];
	    
	    for (int idx = 0; idx < buf.length; ++idx) 
		      buf[idx] = symbols[random.nextInt(symbols.length)];
		    return new String(buf);
	  }

}
