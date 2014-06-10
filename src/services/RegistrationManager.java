package services;

import org.apache.log4j.Logger;

import game.Account;
import dao.AccountJDBC;
import protocol.*;

public class RegistrationManager {
	private static Logger logger = Logger.getLogger(RegistrationManager.class);
	
	public static ServerRegistrationSignal serveRegistration(
			ClientRegistrationSignal signal, AccountJDBC db, Account acc, String remoteIp) {
		ServerRegistrationSignal sSig = new ServerRegistrationSignal();
		String email = signal.email.trim();
		String name = signal.name.trim();
		String pwd = signal.password.trim();
		
		StringBuilder builder = new StringBuilder();
		builder.append("got registration request: email=" + email +" name=" + name +" pwd=" +pwd +" from ip:"+remoteIp);
		
		if (!isValidRegisterInput(email, name, pwd)) {
			sSig.error = RegistrationErrorType.INVALID;
			builder.append(". Failed! invalid input");
			acc = null;
			logger.error(builder.toString());
			return sSig;
		}
		db.connect();
		if (db.getAccountByEmail(email) != null) {
			sSig.error = RegistrationErrorType.EMAIL_ALREADY_EXIST;
			builder.append(". Failed! EMAIL_ALREADY_EXIST");
			acc = null;
		} else if (db.getAccountByName(name) != null) {
			sSig.error = RegistrationErrorType.NAME_ALREADY_EXIST;
			builder.append(". Failed! NAME_ALREADY_EXIST");
			acc = null;
		} else {
			if(db.addAccount(email, name, pwd)){
				Account account = db.getAccountByEmail(email);
				acc.copy(account);
				sSig.registrationOk = true;
				sSig.accountId = acc.id;
				sSig.name = acc.name;
				builder.append(". Succeed! uid:" + acc.id);
			}else{
				builder.append(". Failed! INTERNAL_DB_ERROR");
				sSig.error = RegistrationErrorType.INTERNAL_DB_ERROR;
				acc = null;
			}
		}
		if(sSig.registrationOk){
			logger.info(builder.toString());
		}else{
			logger.error(builder.toString());
		}
		db.finalize();
		return sSig;
	}

	private static boolean isValidRegisterInput(String email, String name,
			String password) {

		if (!email.equals("") && !name.equals("") && !password.equals("")) {
			return true;
		} else {
			return false;
		}
	}
}
