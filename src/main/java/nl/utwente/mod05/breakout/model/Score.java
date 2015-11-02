package nl.utwente.mod05.breakout.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author tristan
 * @since 28-10-15
 */
public class Score {
    private IntegerProperty score = null;
    private StringProperty name = null;

    /**
     * Initialize Score
     * @param name Name of the person
     * @param score Score of the person
     */
    public Score(String name, Integer score){
        this.score = new SimpleIntegerProperty(score);
        this.name = new SimpleStringProperty(name);
    }

    /**
     * Set the score of a person
     * @param score
     */
    public void setScore(Integer score) {
        this.score.setValue(score);
    }

    /**
     * Set the name of a person
     * @param name
     */
    public void setName(String name) {
        this.name.setValue(name);
    }

    /**
     * Return the name of a person
     * @return
     */
    public StringProperty nameProperty(){
        return name;
    }

    /**
     * Return the score of a person
     * @return
     */
    public IntegerProperty scoreProperty(){
        return score;
    }
}
