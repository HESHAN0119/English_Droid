package com.exhera.englishdroid.Dictionary;

public class Dictionary {
    private String word;
    private String description;
    private String userId;
    private String userName;
    private String timeAdded;


    public Dictionary() {
    }

    public Dictionary(String word, String description, String userId, String userName,String timeAdded) {
        this.word = word;
        this.description = description;
        this.userId = userId;
        this.userName = userName;
        this.timeAdded = timeAdded;

    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }
}
