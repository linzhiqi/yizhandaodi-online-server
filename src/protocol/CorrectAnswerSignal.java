package protocol;

public class CorrectAnswerSignal extends ServerSignal {
	public String answer;
	public String standardAnswer;
	public String name;
	public boolean shouldAnswer;
	public String nextQuestion;
	
	public CorrectAnswerSignal(){
		super(SignalTypes.S_CORRECT_ANSWER);
	}
	
	public CorrectAnswerSignal(String answer, String standardAnswer, String name, String nextQuestion, boolean shouldAnswer){
		super(SignalTypes.S_CORRECT_ANSWER);
		this.answer = answer;
		this.standardAnswer = standardAnswer;
		this.name = name;
		this.nextQuestion = nextQuestion;
		this.shouldAnswer = shouldAnswer;
	}
}
