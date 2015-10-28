package nl.utwente.mod05.breakout.helper;

import java.sql.*;
import java.util.*;

/**
 * @author tristan
 * @since 28-10-15
 */
public class DatabaseConnector {

    private static volatile DatabaseConnector instance = null;
    private Connection connection = null;
    private Statement statement = null;
    private String url = "jdbc:mysql://natsirt.nl/ut_breakout";
    private String user = "ut_breakout";
    private String password = "XsJxN39fj6jYuBhw";
    private static final String NAME = "name";
    private static final String SCORE = "score";


    /**
     * Singleton
     * @return
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
     * @return
     */
    public Connection connectDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Get a list of 20 highscores
     * @return
     */
    public List<Map<String, Object>> getHighscores() {
        if(connection == null){
            DatabaseConnector.getInstance().connectDatabase();
        }
        String highScoreStatement = "SELECT * FROM highscores ORDER BY "+SCORE+" DESC LIMIT 20";
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        try {
            PreparedStatement statement = connection.prepareStatement(highScoreStatement);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put(NAME, resultSet.getString(NAME));
                resultMap.put(SCORE, resultSet.getInt(SCORE));
                resultList.add(resultMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        String addScoreStatement = "INSERT INTO highscores ("+NAME+", "+SCORE+") VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(addScoreStatement);
            statement.setString(1, name);
            statement.setInt(2, score);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
