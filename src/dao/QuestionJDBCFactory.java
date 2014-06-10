package dao;

public class QuestionJDBCFactory {

	public QuestionJDBC getQuestionJDBC(){
		return new QuestionJDBCImpl();
	}
}
