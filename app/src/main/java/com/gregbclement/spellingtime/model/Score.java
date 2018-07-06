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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDateDescription() {
        return dateDescription;
    }

    public void setDateDescription(String dateDescription) {
        this.dateDescription = dateDescription;
    }
}
