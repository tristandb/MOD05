package nl.utwente.mod05.breakout.helper;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author tristan
 * @since 28-10-15
 */
public class DatabaseConnectorTest {
    @Test
    public void testSingleton(){
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        assertNotNull("databaseConnector must not be null", databaseConnector);
    }
    @Test
    public void connectDatabase(){
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        assertNotNull("databaseConnector.connectDatabase() must not be null", databaseConnector.connectDatabase());
    }
    @Test
    public void addHighScore(){
        DatabaseConnector databaseConnector = DatabaseConnector.getInstance();
        databaseConnector.connectDatabase();
        databaseConnector.addScore("Tristan", 12);
        assertNotNull("databaseConnector.getHighscores() is not set", databaseConnector.getHighscores());
    }
}
