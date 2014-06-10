package game;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import dao.QuestionJDBCFactory;

import game.logic.GameManager;
import game.logic.Question;
import game.logic.QuestionManager;
import game.states.AnsweringState;
import game.states.GameState;
import game.states.InitialState;
import game.states.PairedState;
import game.states.UnpairedState;
import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import protocol.ServerPairedSignal;
import protocol.ServerQuestionSignal;
import protocol.ServerSignal;
import protocol.SignalTypes;
import server.Session;

public class Game {
	/*
	 * a Game instance will be manipulated by two threads, so multithreading
	 * should be concerned
	 */
	private Session challenger;
	private Session defender;
	private GameState currentState;
	private GameState initialState;
	private GameState unpairedState;
	private GameState pairedState;
	private GameState answeringState;
	private GameManager gM;
	// private QuestionJDBCFactory dbFactory;
	private QuestionManager qManager;
	private Logger logger = Logger.getLogger(Game.class);

	public Game(QuestionManager qM) {
		this.qManager = qM;
		this.initialState = new InitialState();
		this.unpairedState = new UnpairedState(this);
		this.pairedState = new PairedState(this);
		this.answeringState = new AnsweringState(this);
		this.currentState = this.initialState;
	}

	public Game(QuestionManager qM, Session c) {
		this(qM);
		this.currentState = unpairedState;
		this.challenger = c;
	}

	public void setDefender(Session d) {
		this.currentState = this.pairedState;
		this.defender = d;

	}

	public Session getChallenger() {
		return this.challenger;
	}

	public GameManager setGameManager() {
		this.gM = new GameManager(this, this.challenger, this.defender,
				this.qManager);
		return this.gM;
	}

	public GameManager getGameManager() {
		return this.gM;
	}

	public void serveInGameSignal(ClientSignal signal) {
		switch (signal.getSignalType()) {
		case SignalTypes.C_CANCEL_PAIR: {
			this.serveCancelPairSignal(signal);
			break;
		}
		case SignalTypes.C_CLIENT_NEXTGAME: {
			this.serveNextGameSignal(signal);
			break;
		}
		case SignalTypes.C_CLIENT_ANSWER: {
			this.serveAnswerSignal(signal);
			break;
		}
		case SignalTypes.C_CLIENT_QUITGAME: {
			this.serveQuitSignal(signal);
			break;
		}
		}
	}

	private void serveQuitSignal(ClientSignal signal) {
		this.currentState.recvQuitGameSignal(signal);

	}

	public void serveAnswerSignal(ClientSignal signal) {
		if (signal instanceof ClientAnswerSignal) {
			this.currentState.recvAnswerSignal((ClientAnswerSignal) signal);
		} else {
			// log exceptional situation
		}
	}

	public void serveNextGameSignal(ClientSignal signal) {
		this.currentState.recvNextGameSignal(signal);
	}

	public void serveTimeoutSignal(ClientSignal signal) {
		this.currentState.recvClientTimeoutSignal(signal);
	}

	public void serveCancelPairSignal(ClientSignal signal) {
		this.currentState.recvCancelPairSignal(signal);
	}

	public GameState getInitialState() {
		return this.initialState;
	}

	public void setState(GameState state) {
		logger.info("game is set to " + state.toString() + " state");
		this.currentState = state;
	}

	public Session getDefender() {
		return this.defender;
	}

	public GameState getAnsweringState() {
		// TODO Auto-generated method stub
		return this.answeringState;
	}

	public void sendPairedResponse() {
		challenger.send(new ServerPairedSignal(this.defender.getAccount()));
		defender.send(new ServerPairedSignal(this.challenger.getAccount()));

	}

	public void sendPairCanceled() {
		ServerSignal sig = new ServerSignal(SignalTypes.S_PAIRCANCELED);
		challenger.send(sig);
		challenger.send(sig);
	}

	public void timeUp(long id) {
		this.currentState.serverTimerExpired(id);
	}

	public void gameOver() {
		if (challenger != null) {
			this.challenger.unsetGame();
		}
		if (defender != null) {
			this.defender.unsetGame();
		}
	}

	public GameState getCurrentState() {
		// TODO Auto-generated method stub
		return this.currentState;
	}

	public GameState getPairedState() {
		return this.pairedState;
	}

	public void persistResult() {
		this.gM.persistResult();
	}

	public GameState getUnpairedState() {
		return this.unpairedState;
	}

	public void sendPairCanceled(ClientSignal signal) {
		ServerSignal ssig = new ServerSignal(SignalTypes.S_PAIRCANCELED);
		if (signal.sessionId == challenger.getSessionId()) {
			defender.send(ssig);
		} else {
			challenger.send(ssig);
		}

	}

}
