package server.sessionstates;

import protocol.ClientLoginSignal;
import protocol.ClientSignal;
import server.Session;
import services.LoginManager;

public class OfflineState implements SessionState {

	//this state just do nothing
	private Session session;
	public OfflineState(Session session){
		this.session = session;
	}

	@Override
	public void recvLogoffSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvHeartBeatSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvReqPairSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvAllowChallengeSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvBanChallengeSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void serveInGameSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recvChangePwdSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

}
