package protocol;

public class ServerRegistrationSignal extends ServerSignal {
	public boolean registrationOk = false;
	public long accountId;
	public long sessionId;
	public String name;
	public RegistrationErrorType error;
	
	public ServerRegistrationSignal() {
		super();
	}
}
