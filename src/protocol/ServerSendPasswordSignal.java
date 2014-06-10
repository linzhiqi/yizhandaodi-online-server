package protocol;

public class ServerSendPasswordSignal extends ServerSignal {
	
	public boolean sentOK;
	public boolean emailValidated;
	public boolean emailRegistered;
	public boolean emailServiceFailed;

	public ServerSendPasswordSignal(){
		super(SignalTypes.S_SENDPASSWORD);
		
	}

}
