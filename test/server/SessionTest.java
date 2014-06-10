package server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import game.Account;
import game.logic.Question;
import game.states.PairedState;
import game.states.UnpairedState;
import org.mockito.Mockito;
import org.junit.Test;
import dao.QuestionJDBC;
import protocol.ClientSignal;
import protocol.SignalTypes;
import server.sessionstates.BanChallengeState;


public class SessionTest {
	
	


	@Test
	public void testNormalProcess() {
		
		TransportWorker transport = Mockito.mock(TransportWorker.class);
		Account account = new Account(1, "yzdd_test@gmail.com", "testUser","testpass",0,0);
		Session session = new Session(transport,account);
		ClientSignal c_sig = new ClientSignal(SignalTypes.C_REQ_PAIR);
		session.receive(c_sig);
		
		assertTrue(session.getCreatedGame().getChallenger()!=null);
		assertTrue(session.getCreatedGame().getDefender()==null);
		assertSame(session.getCreatedGame().getChallenger().getAccount(),account);
		assertTrue(session.getCreatedGame().getCurrentState() instanceof UnpairedState);
		
		TransportWorker transport2 = Mockito.mock(TransportWorker.class);
		Account account2 = new Account(2, "yzdd_test2@gmail.com", "testUser2","testpass",0,0);
		Session session2 = new Session(transport2,account2);
		ClientSignal c_sig2 = new ClientSignal(SignalTypes.C_REQ_PAIR);
		session2.receive(c_sig2);
		
		assertTrue(session.getCreatedGame().getChallenger()!=null);
		assertTrue(session.getCreatedGame().getDefender()!=null);
		assertSame(session.getCreatedGame().getDefender().getAccount(),account2);
		assertSame(session.getCreatedGame(),session2.getCreatedGame());
		assertTrue(session.getCreatedGame().getCurrentState() instanceof PairedState);
		
		session.receive(new ClientSignal(SignalTypes.C_CANCEL_PAIR));
		
		assertTrue(session.getCreatedGame()==null);
		assertTrue(session2.getCreatedGame()==null);
		assertTrue(session.getCurrentState() instanceof BanChallengeState);
		assertTrue(session2.getCurrentState() instanceof BanChallengeState);
		
		
		session.receive(new ClientSignal(SignalTypes.C_LOG_OFF));
		session2.receive(new ClientSignal(SignalTypes.C_LOG_OFF));
		Mockito.verify(transport).finalize();
		Mockito.verify(transport2).finalize();
		
	}

}
