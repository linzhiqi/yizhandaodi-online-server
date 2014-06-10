package server;

import game.Account;
import game.Game;
import game.PairingQueueManager;
import game.logic.QuestionManager;
import server.sessionstates.*;
import services.OnlineListManager;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

import dao.QuestionJDBCFactory;
import dao.QuestionJDBCImpl;

import protocol.ClientSignal;
import protocol.ServerSignal;

public class Session {
	private TransportWorker transport;
	SignalHandler signalHandler;
	private long sessionId;
	private Account account;
	private SessionState currentState;
	private SessionState lastState;

	private SessionState gamingState;
	private SessionState banChallengeState;
	private SessionState allowChallengeState;
	private SessionState offLineState;
	private Logger logger = Logger.getLogger(Session.class);
	private Game game = null;

	public Session(TransportWorker transportWorker, Account account) {
		this.transport = transportWorker;
		this.account = account;
		this.sessionId = new Random().nextLong();

		gamingState = new GamingState(this);
		banChallengeState = new BanChallengeState(this);
		allowChallengeState = new AllowChallengeState(this);
		offLineState = new OfflineState(this);

		currentState = banChallengeState;
		this.signalHandler = new SignalHandler(this);
	}

	public long getSessionId() {
		return this.sessionId;
	}

	public Account getAccount() {
		return this.account;
	}

	public void send(ServerSignal signal) {
		try {
			transport.send(signal);
		} catch (IOException e) {
			logger.error("send signal:"+signal.getType() + ". uid:" + this.account.id +" failed. " +e.getMessage(), e);
		}
	}

	public void finalize() {
		if (transport != null) {
			transport.finalize();
			transport = null;
		}
		OnlineListManager.getInstance().removePlayer(
				this.sessionId + "-" + this.account.id);
	}

	public Game getCreatedGame() {
		return this.game;
	}

	public void setState(SessionState state) {
		// session can remember if it allowed challenge when come back from
		// gamingState
		if (this.currentState instanceof AllowChallengeState
				|| this.currentState instanceof BanChallengeState) {
			this.lastState = this.currentState;
		}
		this.currentState = state;
	}

	public SessionState getOffLineState() {
		return this.offLineState;
	}

	public SessionState getGamingState() {
		return this.gamingState;
	}

	public SessionState getAllowChallengeState() {
		return this.allowChallengeState;
	}

	public SessionState getBanChallengeState() {
		return this.banChallengeState;
	}

	public SessionState getCurrentState() {
		return this.currentState;
	}

	public SessionState getLastState() {
		return this.lastState;
	}

	public void recvHeartBeatSignal(ClientSignal signal) {
		this.currentState.recvHeartBeatSignal(signal);
	}

	public void recvReqPairSignal(ClientSignal signal) {
		this.currentState.recvReqPairSignal(signal);
	}

	public void recvAllowChallengeSignal(ClientSignal signal) {
		this.currentState.recvAllowChallengeSignal(signal);
	}

	public void recvBanChallengeSignal(ClientSignal signal) {
		this.currentState.recvBanChallengeSignal(signal);
	}

	public void recvLogoffSignal(ClientSignal signal) {
		this.currentState.recvLogoffSignal(signal);
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void registerOnline() {
		OnlineListManager.getInstance().addPlayer(
				this.sessionId + "-" + this.account.id, this);
	}

	// might be interrupted if it waits too long time, or by receiving
	// cancel_pair signal
	public Game getPairGame() {
		PairingQueueManager qM = PairingQueueManager.getInstance();
		synchronized (qM) {
			if (qM.isQueueEmpty()) {
				this.game = new Game(
						new QuestionManager(new QuestionJDBCImpl()), this);
				qM.addOneWaitingPlayer(this);
			} else {
				try {
					this.game = qM.removeWaitingPlayer().getCreatedGame();
					this.game.setDefender(this);
					logger.info("uid:" + this.game.getChallenger().account.id
							+ " uid:" + this.game.getDefender().account.id
							+ " are paired");
					this.game.sendPairedResponse();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					this.game = null;
				}
			}
		}
		return this.game;
	}

	public void relayInGameSignal(ClientSignal signal) {
		// TODO Auto-generated method stub
		this.currentState.serveInGameSignal(signal);
	}

	public void unsetGame() {
		setState(getLastState());
		this.game = null;
	}

	public void setTransport(TransportWorker transportWorker) {
		this.transport = transportWorker;
	}

	public void receive(ClientSignal sig) {
		signalHandler.dispatch(sig);
	}

	public void recvChangePwdSignal(ClientSignal signal) {
		this.currentState.recvChangePwdSignal(signal);
	}
}
