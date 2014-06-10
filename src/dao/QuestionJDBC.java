package dao;

import game.logic.Question;

import java.util.List;

public interface QuestionJDBC {
	public void connect();
	public void finalize();
	public List<Question> getQuestion(int difficulty, int num);
}
