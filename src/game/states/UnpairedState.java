package game.states;

import org.apache.log4j.Logger;

import game.Game;
import game.PairingQueueManager;
import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import server.Session;

public class UnpairedState implements GameState {
	private Logger logger = Logger.getLogger(UnpairedState.class);
	private Game game;

	public UnpairedState(Game game) {
		this.game = game;
	}

	@Override
	public void recvCancelPairSignal(ClientSignal signal) {
		logger.info("uid:"+signal.accountId + " is canceling pair request");
		this.game.setState(this.game.getInitialState());
		Session cSession = this.game.getChallenger();
		PairingQueueManager qM = PairingQueueManager.getInstance();
		synchronized (qM) {
			try {
				if (!qM.isQueueEmpty()) {
					Session session = qM.removeWaitingPlayer();
					if (!(session.getSessionId() == cSession
							.getSessionId())) {
						// log exceptional situation
					}
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		game.gameOver();
	}

	@Override
	public void recvAnswerSignal(ClientAnswerSignal signal) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}

	@Override
	public void recvQuitGameSignal(ClientSignal signal) {
		// nothing TODO
		
	}
	
	@Override
	public String toString(){
		return "Unpaired";
	}

}
