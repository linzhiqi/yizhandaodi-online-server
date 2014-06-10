package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;

import game.Account;

public class AccountJDBCImpl implements AccountJDBC{
	private Connection conn;
	
	@Override
	public void connect(){
		try {
			conn = getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void finalize(){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

	private Connection getConnection() throws SQLException {

		try {
			// The newInstance() call is a work around for some
			// broken Java implementations
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			// handle the error
		}
		return conn = DriverManager
				.getConnection("jdbc:mysql://localhost:3306/yzdd_account?user=accountadmin&password=pass&useUnicode=true&characterEncoding=UTF-8");
	}

	private Account getAccount(long id) {
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("SELECT name,email,password,win,lose FROM account WHERE id = ?");
			st.setLong(1, id);
			rs = st.executeQuery();
			rs.next();
			Account account = new Account(id,rs.getString("email"),rs.getString("name"),rs.getString("password"),rs.getLong("win"),rs.getLong("lose"));
			return account;

		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
			}
		}

	}

	public Account getAccountByName(String name) {
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("SELECT id,email,password,win,lose FROM account WHERE name = ?");
			st.setString(1, name);
			rs = st.executeQuery();
			rs.next();
			Account account = new Account(rs.getLong("id"),rs.getString("email"),name,rs.getString("password"),rs.getLong("win"),rs.getLong("lose"));
			return account;

		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	public Account getAccountByEmail(String email) {
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("SELECT id,name,password,win,lose FROM account WHERE email = ?");
			st.setString(1, email);
			rs = st.executeQuery();
			rs.next();
			Account account = new Account(rs.getLong("id"),email,rs.getString("name"),rs.getString("password"),rs.getLong("win"),rs.getLong("lose"));
			return account;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
			}
		}
	}

	private boolean changePassword(long id, String pwd) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE account SET password = ? WHERE id = ?");
			st.setString(1, pwd);
			st.setLong(2, id);
			st.executeUpdate();

			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public boolean addAccount(String name, String email, String pwd, long win,
			long lose) {
		PreparedStatement st = null;
		try {

			st = conn
					.prepareStatement("INSERT INTO account (name,email,password,win,lose) VALUES (?,?,?,?,?)");
			st.setString(1, name);
			st.setString(2, email);
			st.setString(3, pwd);
			st.setLong(4, win);
			st.setLong(5, lose);

			st.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void getRankingList(TreeSet<Account> list) {
		list.clear();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("SELECT id,name,win,lose FROM account");
			rs = st.executeQuery();
			while(rs.next()){
				list.add(new Account(rs.getLong("id"),"",rs.getString("name"),"",rs.getLong("win"),rs.getLong("lose")));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				st.close();
			} catch (SQLException e) {
			}
		}
	}
	
	public void updateWinRecord(long id, boolean flag){
		
		PreparedStatement st = null;
		String stateMent = null;
		if(flag){
			stateMent = "UPDATE account set win=win+1 where id=?";
		}else{
			stateMent = "UPDATE account set lose=lose+1 where id=?";
		}
		try {
			st = conn.prepareStatement(stateMent);
			st.setLong(1, id);
			st.executeUpdate();			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public boolean addAccount(String email, String name, String password) {
		PreparedStatement st = null;
		try {

			st = conn
					.prepareStatement("INSERT INTO account (name,email,password) VALUES (?,?,?)");
			st.setString(1, name);
			st.setString(2, email);
			st.setString(3, password);

			st.executeUpdate();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	@Override
	public boolean updatePasswordById(long id, String newPassword) {
		return changePassword(id, newPassword);
	}
	@Override
	public Account getAccountByUid(long id) {
		// TODO Auto-generated method stub
		return getAccount(id);
	}
}
