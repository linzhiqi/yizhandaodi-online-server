package protocol;

public class ServerChangePasswordSignal extends ServerSignal {

	public boolean pwdChangeOK;
	public boolean currentPwdIncorrect;
	public boolean dbFailed;
	public boolean PwdTooLong;
	
	public ServerChangePasswordSignal(){
		super(SignalTypes.S_CHANGE_PWD);
	}

}
