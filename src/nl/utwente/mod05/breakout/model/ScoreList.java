package nl.utwente.mod05.breakout.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import nl.utwente.mod05.breakout.helper.DatabaseConnector;

import java.util.List;
import java.util.Map;

/**
 * @author Tristan
 * @since 28-10-15
 */
public class ScoreList {
    private ObservableList<Score> scoreObservableList = FXCollections.observableArrayList();

    /**
     * Constructor, retreive data from the server
     */
    public ScoreList(){
        DatabaseConnector db = DatabaseConnector.getInstance();
		if (db != null) {
			List<Map<String, Object>> highscores = db.getHighscores();
			for(Map<String, Object> highscore: highscores) {
				scoreObservableList.add(new Score(highscore.get("name").toString(), (Integer) highscore.get("score")));
			}
		}
    }

    /**
     * Return ObservableList of Highscores
     */
    public ObservableList<Score> getScoreList(){
        return scoreObservableList;
    }
}
