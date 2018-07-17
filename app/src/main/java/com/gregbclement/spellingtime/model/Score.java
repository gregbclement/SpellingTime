package com.gregbclement.spellingtime.model;

import java.util.Date;

/**
 * @author g.clement
 * Data model representing a score a student earns while spelling a word
 */
public class Score {
    private Date date;
    private Integer score;
    private String dateDescription;

    /**
     * Gets the date the score was created
     * @return Date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date the score was created
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Gets the numeric score from 1 to 5
     * @return
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Sets a score between 1 and 5
     * @param score
     * @throws Exception
     */
    public void setScore(Integer score) throws Exception {
        if(score < 1 || score > 5) {
            throw    new Exception("Score must be between 1 and 5");
        }
        this.score = score;
    }

    /**
     * Gets a string representation of the date
     * @return
     */
    public String getDateDescription() {
        return dateDescription;
    }

    /**
     * Sets the string representation of the date
     * @param dateDescription
     */
    public void setDateDescription(String dateDescription) {
        this.dateDescription = dateDescription;
    }
}
