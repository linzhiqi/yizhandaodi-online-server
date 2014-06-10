package protocol;

import game.Account;

public class ServerPairedSignal extends ServerSignal {

	public String name;
	public long win;
	public long lose;
	public ServerPairedSignal(Account competetor){
		super(SignalTypes.S_PAIRED);
		this.name = competetor.name;
		this.win = competetor.win;
		this.lose = competetor.lose;
	}
	
	public ServerPairedSignal(){
		super(SignalTypes.S_PAIRED);
	}
}
