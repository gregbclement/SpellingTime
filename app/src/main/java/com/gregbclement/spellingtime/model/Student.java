package com.gregbclement.spellingtime.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

/**
 * @author g.clement
 * Data model representing a student
 */
public class Student implements Serializable {
    private String name;
    private String id;
    private String itemType;
    private Boolean inactive;
    private String picture;
    private transient Bitmap pictureBitmap;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public    Bitmap getPictureBitmap() {
        if(picture == "") {
            return null;
        }

        try {
            if (pictureBitmap == null) {
                byte[] imageBytes = Base64.decode(picture, Base64.DEFAULT);
                pictureBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            return pictureBitmap;
        }catch(Exception ex) {}

        return null;
    }
}
