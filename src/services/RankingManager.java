package services;


import game.Account;
import dao.AccountJDBCImpl;
import java.util.Comparator;
import java.util.TreeSet;


public class RankingManager {
	
	private static RankingManager manager = new RankingManager();
	private CreditAlgorithm alg;
	private TreeSet<Account> rankingList;
	
	private RankingManager(){
		this.alg = new CreditAlgorithmImpl();
	}
	
	public static RankingManager getInstance(){
		return manager;
	}
	
	public void init(){
		//load all the accounts' info and sorted on the credits
		//
	}
	
	public TreeSet<Account> createRankingList(){
		TreeSet<Account> list = new TreeSet<Account>(new CreditComparator());
		AccountJDBCImpl db = new AccountJDBCImpl();
		db.connect();
		db.getRankingList(list);
		db.finalize();
		return list;
	}
	
	class CreditComparator implements Comparator<Account>{
		@Override
		public int compare(Account arg0, Account arg1) {
			long credit0 = RankingManager.this.alg.getCredit(arg0.win, arg0.lose);
			long credit1 = RankingManager.this.alg.getCredit(arg1.win, arg1.lose);
			return new Long(credit0).compareTo(credit1);
		}		
	}
	

}
