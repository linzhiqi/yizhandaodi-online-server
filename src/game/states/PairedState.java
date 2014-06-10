package game.states;

import org.apache.log4j.Logger;

import game.Game;
import game.logic.GameManager;
import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import protocol.ServerGameResultSignal;
import protocol.ServerSignal;
import protocol.SignalTypes;
import server.Session;

public class PairedState implements GameState {
	private Game game;
	private volatile boolean onePlayerReady = false;
	private long last_uid;
	private Logger logger = Logger.getLogger(PairedState.class);
	
	public PairedState(Game game) {
		this.game = game;
	}

	@Override
	public void recvCancelPairSignal(ClientSignal signal) {
		//this.game.setState(this.game.getInitialState());
		logger.info("uid:"+signal.accountId + " is canceling pair");
		this.game.setState(this.game.getUnpairedState());
		this.game.sendPairCanceled(signal);
		this.game.gameOver();
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
		if (this.onePlayerReady) {
			logger.info("uid:" + signal.accountId + " sent NextGameSignal. OnePlayerReady is already true, go into answeringState");
			if (this.last_uid != signal.getAccountId()) {
				this.onePlayerReady = false;
				this.game.setState(game.getAnsweringState());
				GameManager gM = this.game.setGameManager();
				gM.getNextQuestion();
				gM.sendQuestion();
				gM.startTimer(GameManager.TIME + GameManager.ALLOWED_NETWORK_LATENCY);
			}else{
				//the same player sends twice, ignore it
			}
		} else {
			logger.info("uid:" + signal.accountId + " sent NextGameSignal. OnePlayerReady will be true");
			this.last_uid = signal.getAccountId();
			this.onePlayerReady = true;
		}
	}

	@Override
	public void recvQuitGameSignal(ClientSignal signal) {
		if(onePlayerReady){
			logger.info("uid:" + signal.accountId + " sent QuitGameSignal");
			onePlayerReady=false;	
		}else{
			//impossible
			logger.error("uid:" + signal.accountId + " sent QuitGameSignal when onePlayReady==false");
		}
		
	}
	
	@Override
	public String toString(){
		return "Paired";
	}

}
