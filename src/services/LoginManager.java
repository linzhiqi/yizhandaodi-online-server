package services;

import org.apache.log4j.Logger;

import game.Account;
import dao.AccountJDBC;
import dao.AccountJDBCImpl;
import protocol.ClientLoginSignal;
import protocol.LoginErrorType;
import protocol.ServerLoginSignal;
import protocol.ServerSignal;
import server.GameServer;
import server.Session;
import server.TransportWorker;

public class LoginManager {

	private static Logger logger = Logger.getLogger(LoginManager.class);

	public static ServerLoginSignal serveLogin(ClientLoginSignal signal, AccountJDBC db, Account acc, String remoteIp) {
		
		ServerLoginSignal serverSignal = new ServerLoginSignal();
		
		String email = signal.getEmail().trim();
		String pwd = signal.getPassword().trim();
		db.connect();
		Account account = db.getAccountByEmail(email);
		db.finalize();
		
		if (account == null) {
			acc = null;
			LoginErrorType error = LoginErrorType.EMAIL_NOT_EXIST;
			serverSignal.setIsOk(false);
			serverSignal.setError(error);
		} else if (account.pwd.trim().equals(pwd)) {
			acc.copy(account);		
			serverSignal.setIsOk(true);
			serverSignal.setAccountId(account.id);
			serverSignal.name = account.name;
			serverSignal.win = acc.win;
			serverSignal.lose = acc.lose;
		} else {
			acc = null;
			LoginErrorType error = LoginErrorType.PASSWORD_NOT_MATCH;
			serverSignal.setIsOk(false);
			serverSignal.setError(error);
		}
		if(serverSignal.isOk()){
			logger.info("got login request for email:" + email + " from ip:" + remoteIp +" and succeed. uid="+serverSignal.accountId);
		}else{
			logger.info("got login request for email:" + email + " from ip:" + remoteIp +" and failed");
		}
		
		
		return serverSignal;
	}
}
