package com.exhera.englishdroid.ShortNote;

import com.google.firebase.Timestamp;

public class ShortNote {

    private String title;
    private String description;
    private String imageUrl;
    private String userId;
    private Timestamp timeAdded;
    private  String userName;
    private String docName;



    public ShortNote() {
    }

    public ShortNote(String title, String description, String imageUrl, String userId, Timestamp timeAdded, String userName,String docName) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
        this.docName = docName;

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Timestamp timeAdded) {
        this.timeAdded = timeAdded;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }



}
