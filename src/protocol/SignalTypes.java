package protocol;

public class SignalTypes {

	/**********************/
	/*signals from clients*/
	/**********************/
	public final static int C_HEART_BEAT = 0;
	public final static int C_LOG_IN = 1;
	public final static int C_LOG_OFF = 2;
	public final static int C_ALLOW_CHALLENGE = 3;
	public final static int C_BAN_CHALLENGE = 4;
	public final static int C_REQ_PAIR = 5;
	public final static int C_CANCEL_PAIR = 6;
	public final static int C_CLIENT_ANSWER = 7;
	public final static int C_CLIENT_TIMEOUT = 8;
	public final static int C_CLIENT_NEXTGAME = 9;
	public final static int C_CLIENT_QUITGAME = 10;
	public final static int C_REGISTRATION = 11;
	public final static int C_SEND_PWD = 18;
	public final static int C_CHANGE_PWD = 20;
	
	/**********************/
	/*signals from server*/
	/**********************/
	//public final static int S_GAME_DESTROYED = 11;
	public final static int S_QUESTION = 12;
	public final static int S_PAIRED = 13;
	public static final int S_PAIRCANCELED = 14;
	public final static int S_INCORRECT_ANSWER = 15;
	public final static int S_CORRECT_ANSWER = 16;
	public final static int S_GAMEOVER = 17;
	public final static int S_SENDPASSWORD = 19;
	public final static int S_CHANGE_PWD = 21;
	
}
