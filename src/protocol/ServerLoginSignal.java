package protocol;

public class ServerLoginSignal extends ServerSignal {
	
	boolean loginOk = false;
	public long accountId;
	public long sessionId;
	public String name;
	public long win;
	public long lose;
	public LoginErrorType error;

	public ServerLoginSignal() {
		super();
	}
	
	public void setIsOk(boolean isOk) {
		this.loginOk = isOk;
	}

	public void setAccountId(long id) {
		this.accountId = id;
	}
	public long getAccountId(){
		return this.accountId;
	}

	public void setSessionId(long id) {
		this.sessionId = id;
	}
	public long getSessionId(){
		return this.sessionId;
	}

	public void setError(LoginErrorType error) {
		this.error = error;
	}
	public LoginErrorType getError(){
		return this.error;
	}

	public boolean isOk(){
		return this.loginOk;
	}
}