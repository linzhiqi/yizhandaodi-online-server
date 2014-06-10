package server;

import java.io.IOException;

import org.apache.log4j.Logger;

import protocol.ClientAnswerSignal;
import protocol.ClientLoginSignal;
import protocol.ClientSignal;
import protocol.SignalTypes;
import server.sessionstates.*;

public class SignalHandler {

	private Session session;
	
	public SignalHandler(Session session) {
		this.session = session;
	}

	public void dispatch(ClientSignal signal) {

		switch (signal.getSignalType()) {
		case SignalTypes.C_HEART_BEAT: {
			session.recvHeartBeatSignal(signal);
			break;
		}
		case SignalTypes.C_CHANGE_PWD: {
			session.recvChangePwdSignal(signal);
			break;
		}
		case SignalTypes.C_REQ_PAIR: {
			session.recvReqPairSignal(signal);
			break;
		}
		case SignalTypes.C_ALLOW_CHALLENGE: {
			session.recvAllowChallengeSignal(signal);
			break;
		}
		case SignalTypes.C_BAN_CHALLENGE: {
			session.recvBanChallengeSignal(signal);
			break;
		}
		case SignalTypes.C_LOG_OFF: {
			session.recvLogoffSignal(signal);
			
			break;
		}

		default: {
			session.relayInGameSignal(signal);
			break;
		}
		}
	}

}
