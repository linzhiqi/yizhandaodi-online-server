package server.sessionstates;

import protocol.ClientSignal;

public interface SessionState {

	public void recvLogoffSignal(ClientSignal signal);
	public void recvHeartBeatSignal(ClientSignal signal);
	public void recvReqPairSignal(ClientSignal signal);
	public void recvAllowChallengeSignal(ClientSignal signal);
	public void recvBanChallengeSignal(ClientSignal signal);
	public void serveInGameSignal(ClientSignal signal);
	public void recvChangePwdSignal(ClientSignal signal);
}
