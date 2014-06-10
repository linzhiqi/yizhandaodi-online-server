package protocol;

public class ClientRegistrationSignal extends ClientSignal {
	public String name;
	public String email;
	public String password;

	public ClientRegistrationSignal(String email, String name, String pwd) {
		super();
		super.signalType = SignalTypes.C_REGISTRATION;
		this.name = name;
		this.email = email;
		this.password = pwd;
	}
}
