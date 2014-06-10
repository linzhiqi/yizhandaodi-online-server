package dao;

import game.Account;
import game.logic.Question;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionJDBCImpl implements QuestionJDBC{
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
	public void finalize() {
		if (conn != null) {
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
				.getConnection("jdbc:mysql://localhost:3306/yzdd_question?user=questionadmin&password=pass");
	}
	@Override
	public List<Question> getQuestion(int difficulty, int num) {
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Question> qList = new ArrayList<Question>();
		try {
			st = conn
					.prepareStatement("SELECT question,answers,checkType FROM question_repo WHERE difficulty=? ORDER BY RAND() LIMIT ?");
			st.setInt(1, difficulty);
			st.setInt(2, num);
			rs = st.executeQuery();
			while (rs.next()) {
				qList.add(new Question(rs.getString("question"), rs
						.getString("answers"), rs.getInt("checkType")));
			}
			return qList;

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


	public boolean upDateQuestion(long qid, String question) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE question_repo SET question = ? WHERE id = ?");
			st.setString(1, question);
			st.setLong(2, qid);
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
	
	public boolean upDateAnswers(long qid, String answers) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE question_repo SET answers = ? WHERE id = ?");
			st.setString(1, answers);
			st.setLong(2, qid);
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
	
	public boolean upDateDifficulty(long qid, int difficulty) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE question_repo SET difficulty = ? WHERE id = ?");
			st.setInt(1, difficulty);
			st.setLong(2, qid);
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
	
	public boolean upDateCategory(long qid, int category) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE question_repo SET category = ? WHERE id = ?");
			st.setInt(1, category);
			st.setLong(2, qid);
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
	
	public boolean upDateCheckType(long qid, int checkType) {
		PreparedStatement st = null;
		try {
			st = conn
					.prepareStatement("UPDATE question_repo SET checkType = ? WHERE id = ?");
			st.setInt(1, checkType);
			st.setLong(2, qid);
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

	public boolean addQuestion( String question, String answers, int difficulty, int category, int checkType ) {
		PreparedStatement st = null;
		try {

			st = conn
					.prepareStatement("INSERT INTO question_repo (question,answers,difficulty,category,checkType) VALUES (?,?,?,?,?)");
			st.setString(1, question);
			st.setString(2, answers);
			st.setInt(3, difficulty);
			st.setInt(4, category);
			st.setInt(5, checkType);

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

}
