package protocol;

import java.io.Serializable;

public class ClientSignal implements Serializable {

	/**
	 * 
	 */
	static final long serialVersionUID = -5647537347486485932L;
	public int signalType;
	public long accountId;
	public long sessionId;
	
	public ClientSignal(){
		
	}
	
	public ClientSignal(int type){
		signalType = type;
	}

	public int getSignalType(){
		return this.signalType;
	}
	public void setSignalType(int type){
		this.signalType = type;
	}
	
	public long getAccountId(){
		return this.accountId;
	}
	public void setAccountId(long id){
		this.accountId = id;
	}
	
	public long getSessionId(){
		return this.sessionId;
	}
	
	public void setSessionId(long id){
		this.sessionId = id;
	}
}
