package game.states;

import protocol.ClientAnswerSignal;
import protocol.ClientSignal;

public interface GameState {

	public void recvCancelPairSignal(ClientSignal signal);
	public void recvAnswerSignal(ClientAnswerSignal signal);
	public void recvClientTimeoutSignal(ClientSignal signal);
	public void serverTimerExpired(long id);
	public void recvNextGameSignal(ClientSignal signal);
	public void recvQuitGameSignal(ClientSignal signal);
	public String toString();
	
}
