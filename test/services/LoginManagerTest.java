package services;

import static org.junit.Assert.*;
import game.Account;
import java.io.IOException;
import org.junit.Test;
import dao.AccountJDBC;
import protocol.ClientLoginSignal;
import protocol.LoginErrorType;
import protocol.ServerLoginSignal;

public class LoginManagerTest {
	
	class AccountJDBCDummyImpl implements AccountJDBC{

		Account account = null;
		@Override
		public Account getAccountByEmail(String whatever) {
			return account;
		}

		public void setAccount(long id, String email, String name, String password, long win, long lose){
			account = new Account(id, email, name, password, win, lose);
		}
		@Override
		public void connect() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void finalize(){
			
		}

		@Override
		public Account getAccountByName(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean addAccount(String email, String name, String password) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean updatePasswordById(long id, String newPassword) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Account getAccountByUid(long id) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	@Test
	public void testServeLoginRightPwd() throws IOException {
		
		ClientLoginSignal sig = new ClientLoginSignal();
		sig.setEmail("yzdd_test@gmail.com");
		sig.setPassword("testpass");
		
		AccountJDBCDummyImpl db = new AccountJDBCDummyImpl();
		db.setAccount(1, "yzdd_test@gmail.com", "testUser","testpass",0,0);
		assertTrue(db.getAccountByEmail("anyString")!=null);
		Account acc = new Account();
		ServerLoginSignal serverSig = null;
		serverSig = LoginManager.serveLogin(sig, db, acc, "dummyIP");
		
		assertEquals(1,serverSig.getAccountId());
		assertTrue(serverSig.isOk());
		assertTrue(acc!=null);
	}
	@Test
	public void testServeeLoginWrongPwd(){
		ClientLoginSignal sig = new ClientLoginSignal();
		sig.setEmail("yzdd_test@gmail.com");
		sig.setPassword("wrongpass");
		
		AccountJDBCDummyImpl db = new AccountJDBCDummyImpl();
		db.setAccount(1, "yzdd_test@gmail.com", "testUser","testpass",0,0);
		Account acc = new Account();
		ServerLoginSignal serverSig = null;
		serverSig = LoginManager.serveLogin(sig, db, acc, "dummyIP");
		
		assertEquals(LoginErrorType.PASSWORD_NOT_MATCH,serverSig.getError());
		assertTrue(!serverSig.isOk());
		assertTrue(acc.isEmpty());
	}
	@Test
	public void testServeeLoginNoAccount(){
		ClientLoginSignal sig = new ClientLoginSignal();
		sig.setEmail("yzdd_test@gmail.com");
		sig.setPassword("wrongpass");
		
		AccountJDBCDummyImpl db = new AccountJDBCDummyImpl();
		Account acc = new Account();
		ServerLoginSignal serverSig = null;
		serverSig = LoginManager.serveLogin(sig, db, acc, "dummyIP");
		
		assertEquals(LoginErrorType.EMAIL_NOT_EXIST,serverSig.getError());
		assertTrue(!serverSig.isOk());
		assertTrue(acc.isEmpty());
	}
}
