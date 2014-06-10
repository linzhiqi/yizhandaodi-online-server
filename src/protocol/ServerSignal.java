package protocol;

import java.io.Serializable;

public class ServerSignal implements Serializable{


	/**
	 * 
	 */
	static final long serialVersionUID = -585388264432630255L;
	/**********************/
	/*sginals from server */
	/**********************/
	int signalType;
	
	public ServerSignal(){
		
	}
	
	public ServerSignal(int type){
		this.signalType = type;
	}
	
	public int getType(){
		return this.signalType;
	}
}
