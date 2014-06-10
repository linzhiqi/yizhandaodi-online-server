package protocol;

public class ClientChangePasswordSignal extends ClientSignal {

	public String currentPassword;
	public String newPassword;
	
	public ClientChangePasswordSignal(String cPwd, String nPwd) {
		super(SignalTypes.C_CHANGE_PWD);
		this.currentPassword = cPwd;
		this.newPassword = nPwd;
	}

}
