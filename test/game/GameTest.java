package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import game.logic.Question;
import game.logic.QuestionManager;
import game.states.AnsweringState;
import game.states.PairedState;
import game.states.UnpairedState;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;

import dao.QuestionJDBC;

import protocol.ClientAnswerSignal;
import protocol.ClientSignal;
import protocol.ServerGameResultSignal;
import protocol.ServerQuestionSignal;
import protocol.ServerSignal;
import protocol.SignalTypes;

import server.Session;

public class GameTest {
	
class QuestionJDBCDummyImpl implements QuestionJDBC{
		
		@Override
		public void connect() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void finalize(){
			
		}

		@Override
		public List<Question> getQuestion(int difficulty, int num) {
			ArrayList<Question> list = new ArrayList<Question>();
			Question q = new Question("谁是猪", "我#他#wo#ta", 1);
			list.add(q);
			return list;
		}
	}

	@Test
	public void testNormalProcess() {
		/*QuestionJDBCDummyImpl dummyQDB = new QuestionJDBCDummyImpl();
		QuestionJDBCFactory factory = Mockito.mock(QuestionJDBCFactory.class);
		Mockito.when(factory.getQuestionJDBC()).thenReturn(dummyQDB);
		*/
		
		QuestionManager qM = mock(QuestionManager.class);
		when(qM.getNextQuestion()).thenReturn(new Question("谁是猪", "我#他#wo#ta", 1));
		Session session1 = mock(Session.class);
		Session session2 = mock(Session.class);
		when(session1.getAccount()).thenReturn(new Account(1, "yzdd_test@gmail.com", "testUser","testpass",0,0));
		when(session2.getAccount()).thenReturn(new Account(2, "yzdd_test2@gmail.com", "testUser2","testpass",0,0));
		ArgumentCaptor<ServerSignal> savedCaptor = ArgumentCaptor.forClass(ServerSignal.class);
		
		Game game = new Game(qM,session1);
		
		assertTrue(game.getCurrentState() instanceof UnpairedState);
		
		game.setDefender(session2);
		
		assertTrue(game.getCurrentState() instanceof PairedState);
		
		ClientSignal sig1 = new ClientSignal(SignalTypes.C_CLIENT_NEXTGAME);
		sig1.setAccountId(1);
		game.serveNextGameSignal(sig1);
		
		assertTrue(game.getCurrentState() instanceof PairedState);
		
		verify(session1, never()).send(any(ServerQuestionSignal.class));
		verify(session2, never()).send(any(ServerQuestionSignal.class));	
		
		ClientSignal sig2 = new ClientSignal(SignalTypes.C_CLIENT_NEXTGAME);
		sig2.setAccountId(2);
		game.serveNextGameSignal(sig2);
		
		
		assertTrue(game.getCurrentState() instanceof AnsweringState);
		verify(session1).send(savedCaptor.capture());
		assertTrue(savedCaptor.getValue().getType()==SignalTypes.S_QUESTION);
		verify(session2).send(savedCaptor.capture());
		assertTrue(savedCaptor.getValue().getType()==SignalTypes.S_QUESTION);
		assertEquals(1,game.getGameManager().getCounter());
		
		ClientAnswerSignal answer1 = new ClientAnswerSignal("wo");
		answer1.setAccountId(1);
		game.serveAnswerSignal(answer1);
		
		assertEquals(2,game.getGameManager().getCounter());
		
		verify(session2, times(3)).send(savedCaptor.capture());
		assertTrue(savedCaptor.getValue().getType()==SignalTypes.S_QUESTION);
		
		try {
			Thread.sleep(45000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		verify(session2,times(4)).send(savedCaptor.capture());
		assertTrue(savedCaptor.getValue().getType()==SignalTypes.S_GAMEOVER);
		
	}

}
