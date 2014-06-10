package protocol;

public class ClientLoginSignal extends ClientSignal {

	String name;
	String email;
	String password;
	
	public ClientLoginSignal(){
		super();
		super.signalType = SignalTypes.C_LOG_IN;
		super.sessionId = -1;
		super.accountId = -1;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public String getEmail(){
		return this.email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getPassword(){
		return this.password;
	}
	public void setPassword(String pwd){
		this.password = pwd;
	}
}
