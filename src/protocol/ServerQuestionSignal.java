package protocol;

public class ServerQuestionSignal extends ServerSignal {

	public String question;
	public boolean shouldAnswer;
	public ServerQuestionSignal(){
		super(SignalTypes.S_QUESTION);
	}

	public ServerQuestionSignal(String question, boolean answer) {
		this();
		this.question = question;
		this.shouldAnswer = answer;
	}
}
