package com.gregbclement.spellingtime;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class SpellingList implements Comparable<SpellingList>, Serializable {
    private String name;
    private String studentId;
    private Date createdDate;
    private String itemType;
    private Boolean inactive;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(@NonNull SpellingList o) {
        return -this.getCreatedDate().compareTo(o.getCreatedDate());
    }
}
