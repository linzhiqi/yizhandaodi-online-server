package game.logic;

import game.Game;

import java.util.Timer;
import java.util.TimerTask;

import dao.AccountJDBCImpl;

import protocol.CorrectAnswerSignal;
import protocol.IncorrectAnswerSignal;
import protocol.ServerGameResultSignal;
import protocol.ServerQuestionSignal;
import server.Session;

public class GameManager {
	public static int TIME = 40000;
	public static int ANSWER_SHOWING_TIME = 2000;
	public static int ALLOWED_NETWORK_LATENCY = 4000;
	private Game game;
	private QuestionManager qM;
	private Session answer;
	private Session watch;
	private Question question;
	private int difficulty= 1;
	//20 easy + 20 mediate + 20 difficult, in order
	private int counter;
	Timer timer;
	
	public GameManager(Game game, Session c, Session d, QuestionManager qM){
		this.game = game;
		answer = c;
		watch = d;
		this.qM = qM;
	}
	
	public Question getNextQuestion(){
		//System.out.println("GameManager, counter = " + counter);
		if(counter<21){
			difficulty = 1;
		}else if(counter<41){
			difficulty = 2;
		}else{
			difficulty = 3;
		}
		
		this.question = qM.getNextQuestion();
		if(this.question==null){
			qM.cacheSomeQuestion(difficulty, QuestionManager.CACHE_SIZE);
			this.question = qM.getNextQuestion();
		}
		this.counter++;	
		return this.question;
	}
	
	public Question getCurrentQuestion(){
		return this.question;
	}
	
	public void nextRound(){
		swap();		
	}
	
	public int getCounter(){
		return this.counter;
	}
	
	private void swap(){
		Session tmp = answer;
		answer = watch;
		watch = tmp;
	}

	
	public void sendQuestion(){		
		answer.send(new ServerQuestionSignal(question.getQuestion(),true));
		watch.send(new ServerQuestionSignal(question.getQuestion(),false));
	}

	public void sendIncorrectAnswer(String an) {
		IncorrectAnswerSignal sig = new IncorrectAnswerSignal(an,answer.getAccount().name);
		answer.send(sig);
		watch.send(sig);
	}

	public void sendCorretAnswer(String an, String standardAnswer, String nextQuestion) {
		answer.send(new CorrectAnswerSignal(an, standardAnswer, answer.getAccount().name,nextQuestion,true));
		watch.send(new CorrectAnswerSignal(an, standardAnswer, answer.getAccount().name,nextQuestion,false));
	}

	public void sendResult(ServerGameResultSignal sig) {
		watch.send(sig);
		answer.send(sig);
	}

	public void startTimer(int millis) {
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				GameManager.this.game.timeUp(answer.getAccount().id);
			}
			}
		, millis);
		
	}
	
	public void cancelTimer() {
		timer.cancel();
		timer = null;
	}
	
	public Session getAnswerer(){
		return this.answer;
	}

	public Session getWatcher(){
		return this.watch;
	}

	public void persistResult() {
		AccountJDBCImpl db = new AccountJDBCImpl();
		db.connect();
		db.updateWinRecord(this.answer.getAccount().id, false);
		db.updateWinRecord(this.watch.getAccount().id, true);
	}
}
