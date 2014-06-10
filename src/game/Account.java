package game;

public class Account {
	public long id=-1;
	public String email;
	public String name;
	public String pwd;
	public long win;
	public long lose;

	
	public Account(long id, String email, String name, String pwd, long win, long lose){
		this.id = id;
		this.email = email;
		this.name = name;
		this.pwd = pwd;
		this.win = win;
		this.lose = lose;
	}
	
	public Account(){
		
	}
	
	public void copy(Account acc){
		id = acc.id;
		email = acc.email;
		name = acc.name;
		pwd = acc.pwd;
		win = acc.win;
		lose = acc.lose;
	}
	
	public boolean isEmpty(){
		return id==-1;
	}

}
