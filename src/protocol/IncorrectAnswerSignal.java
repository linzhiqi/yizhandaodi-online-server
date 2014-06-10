package protocol;

public class IncorrectAnswerSignal extends ServerSignal {
	public String answer;
	public String name;
	
	public IncorrectAnswerSignal(){
		super(SignalTypes.S_INCORRECT_ANSWER);
	}
	
	public IncorrectAnswerSignal(String answer, String answerName){
		super(SignalTypes.S_INCORRECT_ANSWER);
		this.answer = answer;
		this.name = answerName;
	}
	
}
