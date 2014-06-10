package protocol;

public class ServerGameResultSignal extends ServerSignal {
	public long loserId;
	public final static long EVEN = -1;
	public String standardAnswer;
	
	public ServerGameResultSignal(long id, String standardAnswer){
		super(SignalTypes.S_GAMEOVER);
		this.loserId = id;
		this.standardAnswer = standardAnswer;
	}


	public boolean amILoser(long id){
		return this.loserId==id;
	}
}
