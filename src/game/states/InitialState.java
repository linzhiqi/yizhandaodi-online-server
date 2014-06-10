package game.states;

import org.apache.log4j.Logger;

import protocol.ClientAnswerSignal;
import protocol.ClientSignal;

/**
 * this state is a transitional state
 * @author Vita
 *
 */
public class InitialState implements GameState {
	Logger logger = Logger.getLogger(InitialState.class);
	@Override
	public void recvCancelPairSignal(ClientSignal signal) {
		logger.error("uid:"+signal.accountId + " is canceling pair");
	}


	@Override
	public void recvAnswerSignal(ClientAnswerSignal signal) {
		logger.error("uid:"+signal.accountId + " is sending answer signal");

	}

	@Override
	public void recvClientTimeoutSignal(ClientSignal signal) {
		// TODO Auto-generated method stub

	}

	@Override
	public void serverTimerExpired(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void recvNextGameSignal(ClientSignal signal) {
		logger.error("uid:"+signal.accountId + " is sending NextGame signal");

	}


	@Override
	public void recvQuitGameSignal(ClientSignal signal) {
		// nothing TODO
		
	}
	
	@Override
	public String toString(){
		return "Initial";
	}

}
