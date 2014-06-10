package server.sessionstates;

import org.apache.log4j.Logger;

import game.Game;
import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import server.Session;
import services.HeartBeatManager;

public class GamingState implements SessionState {

	private Session session;
	private Logger logger = Logger.getLogger(GamingState.class);
	public GamingState(Session session) {
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
		// TODO Auto-generated method stub

	}

	/******************************************************
	@Override
	public void recvCancelPairSignal(ClientSignal signal) {
		Game game = this.session.getCreatedGame();
		game.serveCancelPairSignal(signal);
		this.session.setState(this.session.getLastState());
	}

	@Override
	public void recvAnswerSignal(ClientSignal signal) {
		if (signal instanceof ClientAnswerSignal) {
			Game game = this.session.getCreatedGame();
			game.serveAnswerSignal((ClientAnswerSignal) signal);
		} else {
			// log down exceptional situation
		}
	}

	@Override
	public void recvClientTimeoutSignal(ClientSignal signal) {
		Game game = this.session.getCreatedGame();
		/*
		 * because we hope when the state change back from gaming, the Game
		 * instance can be GC, soit'd be better to have the game instance to
		 * change the session state when all game logic has been finished.
		 *
		game.serveTimeoutSignal(signal);

	}

*******************************************************************/

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
		Game game = this.session.getCreatedGame();
		if(game!=null){
			game.serveInGameSignal(signal);
		}else{
			logger.info("uid:" + signal.accountId + "sent InGameSignal:" + signal.signalType +", but game already destructed and pair is canceled");
		}
	}

	@Override
	public void recvChangePwdSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		
	}

}
