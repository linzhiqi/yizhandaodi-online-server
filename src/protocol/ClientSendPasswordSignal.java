package protocol;

public class ClientSendPasswordSignal extends ClientSignal {
	public String email;
	
	public ClientSendPasswordSignal(String email){
		super(SignalTypes.C_SEND_PWD);
		this.email = email;
	}
}
