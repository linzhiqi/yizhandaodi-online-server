package dao;

import game.Account;

public interface AccountJDBC {

	public Account getAccountByEmail(String email);
	public Account getAccountByName(String name);
	public boolean addAccount(String email, String name, String password);
	public boolean updatePasswordById(long id, String newPassword);
	public void connect();
	public void finalize();
	public Account getAccountByUid(long id);
}
