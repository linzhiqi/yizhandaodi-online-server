package server.sessionstates;

import dao.AccountJDBC;
import dao.AccountJDBCImpl;
import game.Account;
import game.Game;
import game.PairingQueueManager;
import protocol.ClientChangePasswordSignal;
import protocol.ClientSignal;
import protocol.ServerChangePasswordSignal;
import server.Session;
import services.HeartBeatManager;

public class AllowChallengeState implements SessionState {

	private Session session;
	public AllowChallengeState(Session session){
		this.session = session;
	}

	@Override
	public void recvLogoffSignal(ClientSignal signal) {
		this.session.setState(session.getOffLineState());
		this.session.finalize();
	}

	@Override
	public void recvHeartBeatSignal(ClientSignal signal) {
		HeartBeatManager.getInstance().heartBeatReceived(signal);
	}

	@Override
	public void recvReqPairSignal(ClientSignal signal) {
		
		Game game = this.session.getPairGame();
		//game.serveInGameSignal(signal);
		this.session.setState(this.session.getGamingState());
	}

	@Override
	public void recvAllowChallengeSignal(ClientSignal signal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recvBanChallengeSignal(ClientSignal signal) {
		this.session.setState(this.session.getBanChallengeState());
	}

	@Override
	public void serveInGameSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void recvChangePwdSignal(ClientSignal signal) {
		long uid = ((ClientChangePasswordSignal)signal).accountId;
		String cPwd = ((ClientChangePasswordSignal)signal).currentPassword;
		String nPwd = ((ClientChangePasswordSignal)signal).newPassword;
		ServerChangePasswordSignal sSig = new ServerChangePasswordSignal();
		
		if(cPwd.length()>40 || nPwd.length()>40){
			sSig.pwdChangeOK = false;
			sSig.PwdTooLong = true;
			this.session.send(sSig);
			return;
		}
		sSig.PwdTooLong = false;
		
		AccountJDBC db = new AccountJDBCImpl();
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
		
		this.session.send(sSig);
	}

}
