package protocol;

public class ClientAnswerSignal extends ClientSignal {

	public String answer;
	
	public ClientAnswerSignal(String answer){
		super();
		super.signalType = SignalTypes.C_CLIENT_ANSWER;
		
		this.answer = answer;
	}
	
	public String getAnswer(){
		return this.answer==null?"":this.answer;
	}
}
