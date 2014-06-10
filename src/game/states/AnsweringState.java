package game.states;

import org.apache.log4j.Logger;

import game.Game;
import game.logic.GameManager;
import game.logic.Question;
import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import protocol.ServerGameResultSignal;

public class AnsweringState implements GameState {
	
	private Game game;
	private Logger logger = Logger.getLogger(AnsweringState.class);
	
	public AnsweringState(Game game){
		this.game = game;
	}

	@Override
	public void recvCancelPairSignal(ClientSignal signal) {
		logger.error("uid:"+signal.accountId + " is canceling pair");
	}

	@Override
	public void recvAnswerSignal(ClientAnswerSignal signal) {
		String answer = signal.getAnswer();
		GameManager gM = game.getGameManager();
		Question q = gM.getCurrentQuestion();
		String standardAnswer = q.getStandardAnswer();
		boolean isCorrect = q.verifyAnswer(answer);
		if(!isCorrect){
			logger.info("wrong answer:" + answer + " for question:" + q.getQuestion());
			gM.sendIncorrectAnswer(answer);
		}else{
			gM.cancelTimer();
			gM.nextRound();
			Question question = gM.getNextQuestion();
			gM.sendCorretAnswer(answer,standardAnswer,question.getQuestion());
			gM.startTimer(GameManager.TIME + GameManager.ANSWER_SHOWING_TIME + GameManager.ALLOWED_NETWORK_LATENCY); 
		}
	}

	/**
	 * should be removed, client side don't do timeout signal, server timer is the only judger
	 */
	@Override
	synchronized public void recvClientTimeoutSignal(ClientSignal signal) {
		this.game.getGameManager().cancelTimer();
		this.game.setState(game.getPairedState());
		this.game.persistResult();
		GameManager gM = game.getGameManager();
		Question q = gM.getCurrentQuestion();
		String standardAnswer = q.getStandardAnswer();
		this.game.getGameManager().sendResult(new ServerGameResultSignal(signal.getAccountId(),standardAnswer));
		
	}

	@Override
	synchronized public void serverTimerExpired(long id) {
		this.game.setState(game.getPairedState());
		this.game.persistResult();
		GameManager gM = game.getGameManager();
		gM.cancelTimer();
		Question q = gM.getCurrentQuestion();
		String standardAnswer = q.getStandardAnswer();
		this.game.getGameManager().sendResult(new ServerGameResultSignal(id,standardAnswer));
	}

	@Override
	public void recvNextGameSignal(ClientSignal signal) {
		logger.error("uid:" +  signal.accountId + "is sending NextGameSignal");
	}

	@Override
	public void recvQuitGameSignal(ClientSignal signal) {
		logger.error("uid:" +  signal.accountId + " is sending QuitGameSignal");
	}
	@Override
	public String toString(){
		return "Answering";
	}

}
