package scrabble.dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DictonaryDb {
	
	public static final int MIN_WORD_LEN=2;
	public static final int MAX_WORD_LEN=15;
	public static final String TABLE="WORDS";
	public static final String COL_WORD="word";
	public static final String COL_LETTERS="letters";
	
	private Connection connection=null;
	private Statement batchStatment=null;
	
	private int min;
	private int max;
	
	public void initDb() {
		initDb( MIN_WORD_LEN, MAX_WORD_LEN );
	}
	
	public void initDb( int newMin, int newMax) {
		min = newMin;
		max = newMax;
		// use custom dll location instead of default auto extract to user temp directory 
		// because no need to extract dll file on every use with timestamp like uid in name. Also, sqlite fails deletion of dll file after use
		System.setProperty("org.sqlite.lib.path","D:\\dev\\java\\workspace\\boards\\lib");
		System.setProperty("org.sqlite.lib.name","sqlitejdbc.dll");
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite::memory:");
			Statement statement = connection.createStatement();
			statement.executeUpdate("create table "+TABLE+" ("+COL_WORD+" string, "+COL_LETTERS+" integer)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startBatch() {
		try {
			batchStatment  = connection.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public boolean addToBatch(String word) {
		try {
			batchStatment.addBatch("insert into "+TABLE+" values('"+word.toUpperCase()+"',"+word.length()+")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
    
    public void executeBatch() {
    	try {
			batchStatment.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public boolean testWord(String word) {
    	Statement statement;
		try {
			statement = connection.createStatement();
		    
	        ResultSet rs = statement.executeQuery("select * from "+TABLE+" where "+COL_LETTERS+"="+word.length()+" and "+COL_WORD+"='"+word+"'");
	        return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

    }
    
    public void close() {
    	try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public int getMin() {
		return min;
	}
	
	public int getMax() {
		return max;
	}

}
