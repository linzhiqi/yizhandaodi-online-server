package server.sessionstates;

import org.apache.log4j.Logger;

import dao.AccountJDBC;
import dao.AccountJDBCImpl;
import game.Account;
import game.Game;
import protocol.ClientChangePasswordSignal;
import protocol.ClientSignal;
import protocol.ServerChangePasswordSignal;
import protocol.ServerSendPasswordSignal;
import protocol.ServerSignal;
import protocol.SignalTypes;
import server.Session;
import services.HeartBeatManager;

public class BanChallengeState implements SessionState {

	private Logger logger = Logger.getLogger(BanChallengeState.class);
	private Session session;
	public BanChallengeState(Session session){
		this.session = session;
	}

	@Override
	public void recvLogoffSignal(ClientSignal signal) {
		logger.info("uid:" + signal.accountId + " is logging off");
		this.session.setState(session.getOffLineState());
		this.session.finalize();
	}

	@Override
	public void recvHeartBeatSignal(ClientSignal signal) {
		HeartBeatManager.getInstance().heartBeatReceived(signal);
	}

	@Override
	public void recvReqPairSignal(ClientSignal signal) {
		logger.info("uid:"+signal.accountId+" is requesting pair");
		Game game = this.session.getPairGame();
		//game.serveInGameSignal(signal);
		this.session.setState(this.session.getGamingState());
	}

	@Override
	public void recvAllowChallengeSignal(ClientSignal signal) {
		this.session.setState(this.session.getAllowChallengeState());
	}

	@Override
	public void recvBanChallengeSignal(ClientSignal signal) {
		// TODO Auto-generated method stub

	}
	@Override
	public void serveInGameSignal(ClientSignal signal) {
		ServerSignal ssig = new ServerSignal(SignalTypes.S_PAIRCANCELED);
		logger.info("uid:"+signal.accountId+" is sending InGameSginal:"+signal.signalType+" so paircanceled signal is returned");
		this.session.send(ssig);
	}

	@Override
	public void recvChangePwdSignal(ClientSignal signal) {
		long uid = ((ClientChangePasswordSignal)signal).accountId;
		String cPwd = ((ClientChangePasswordSignal)signal).currentPassword;
		String nPwd = ((ClientChangePasswordSignal)signal).newPassword;
		System.out.println("received uid =" + uid + " , cPwd="+cPwd +" ,nPwd=" + nPwd);
		ServerChangePasswordSignal sSig = new ServerChangePasswordSignal();
		
		if(cPwd.length()>40 || nPwd.length()>40){
			sSig.pwdChangeOK = false;
			sSig.PwdTooLong = true;
			this.session.send(sSig);
			return;
		}
		sSig.PwdTooLong = false;
		
		AccountJDBC db = new AccountJDBCImpl();
		db.connect();
		Account acct = null;
		if((acct = db.getAccountByUid(uid)) != null){
			if(acct.pwd.equals(cPwd)){
				sSig.currentPwdIncorrect = false;
				if(db.updatePasswordById(uid, nPwd)){
					sSig.pwdChangeOK = true;
				}else{
					sSig.dbFailed = true;
				}
			}else{
				sSig.currentPwdIncorrect = true;
				sSig.pwdChangeOK = false;
			}
		}else{
			sSig.pwdChangeOK = false;
			sSig.dbFailed = true;
			sSig.currentPwdIncorrect = false;
		}
		db.finalize();
		
		this.session.send(sSig);
	}

}
