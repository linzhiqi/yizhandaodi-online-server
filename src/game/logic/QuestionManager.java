package game.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dao.QuestionJDBC;
import dao.QuestionJDBCImpl;

public class QuestionManager{

	public static int CACHE_SIZE = 20;
	private QuestionJDBC db;
	
	private LinkedList<Question> qList;
	
	public QuestionManager(QuestionJDBC db){
		this.db = db;
		qList = new LinkedList<Question>();
	}
	
	public void init(){
		refreshQList();
	}
	
	private void refreshQList(){
		
	}
	
	public Question getNextQuestion(){
		Question rtn = null;
		if(qList.isEmpty()){
			return rtn;
		}else{
			rtn = qList.pollFirst();
		}
		return rtn;
	}

	public void cacheSomeQuestion(int difficulty, int cacheSize) {
		qList.clear();
		db.connect();
		List<Question> list = db.getQuestion(difficulty, cacheSize);
		qList.addAll(list);	
		//System.out.println("questionManager, cacheSomeQuestion, qList size = " +qList.size());
		db.finalize();
	}
}
