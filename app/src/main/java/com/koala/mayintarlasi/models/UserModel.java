package com.koala.mayintarlasi.models;

public class UserModel
{
    private String userID;
    private int userName;
    private int score;
    private int difficulty;
    private String date;

    public UserModel(String userID, int score, int difficulty)
    {
        this.userID = userID;
        this.score = score;
        this.difficulty = difficulty;
    }



    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
