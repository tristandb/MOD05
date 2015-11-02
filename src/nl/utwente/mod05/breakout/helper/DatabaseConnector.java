package nl.utwente.mod05.breakout.helper;

import nl.utwente.mod05.breakout.Breakout;

import java.sql.*;
import java.util.*;

/**
 * @author tristan
 * @since 28-10-15
 */
public class DatabaseConnector {
	public static final int LOGIN_TIMEOUT = 5;
    private static volatile DatabaseConnector instance = null;
    private static Connection connection = null;
    private static final String url = "jdbc:mysql://natsirt.nl/ut_breakout";
    private static final String user = "ut_breakout";
    private static final String password = "XsJxN39fj6jYuBhw";
    private static final String NAME = "name";
    private static final String SCORE = "score";


    /**
     * Singleton
     * @return The instance of the Database.
     */
    public static synchronized DatabaseConnector getInstance() {
        if (instance == null) {
            instance = new DatabaseConnector();
        }
        return instance;
    }

    private DatabaseConnector() {
    }

    /**
     * Connect to the database
     * @return connection
     */
    public Connection connectDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            DriverManager.setLoginTimeout(LOGIN_TIMEOUT);
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
			if (Breakout.DEBUG) {
				e.printStackTrace();
			}
        }
        return connection;
    }

    /**
     * Get a list of 20 highscores
     * @return List with highscores
     */
    public List<Map<String, Object>> getHighscores() {
        if(connection == null){
            DatabaseConnector.getInstance().connectDatabase();
        }

		List<Map<String, Object>> resultList = new ArrayList<>();
		if (connection != null) {
			String highScoreStatement = "SELECT * FROM highscores ORDER BY "+SCORE+" DESC LIMIT 20";
			try {
				PreparedStatement statement = connection.prepareStatement(highScoreStatement);
				ResultSet resultSet = statement.executeQuery();
				while (resultSet.next()) {
					Map<String, Object> resultMap = new HashMap<>();
					resultMap.put(NAME, resultSet.getString(NAME));
					resultMap.put(SCORE, resultSet.getInt(SCORE));
					resultList.add(resultMap);
				}
			} catch (SQLException e) {
				if (Breakout.DEBUG) {
					e.printStackTrace();
				}
			}
		}
        return resultList;
    }


    /**
     * Add a score to the database
     * @param name Name
     * @param score Score
     */
    public void addScore(String name, int score) {
        if(connection == null){
            DatabaseConnector.getInstance().connectDatabase();
        }

		if (connection != null) {
			String addScoreStatement = "INSERT INTO highscores ("+NAME+", "+SCORE+") VALUES (?, ?)";
			try {
				PreparedStatement statement = connection.prepareStatement(addScoreStatement);
				statement.setString(1, name);
				statement.setInt(2, score);
				statement.executeUpdate();
			} catch (SQLException e) {
				if (Breakout.DEBUG) {
					e.printStackTrace();
				}
			}
		}
    }
}
