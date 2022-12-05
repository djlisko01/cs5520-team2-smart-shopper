package com.example.smartshopper.models;

public class UserScore {
    int totalThumbsUp;
    int totalThumbsDown;
    float thumbsUpRatio;

    public UserScore() {
    }

    public UserScore(int totalThumbsUp, int totalThumbsDown){
        this.totalThumbsUp = totalThumbsUp;
        this.totalThumbsDown = totalThumbsDown;
        this.thumbsUpRatio = this.calculateThumbsUpRatio();
    }

    public int getTotalThumbsUp() {
        return totalThumbsUp;
    }

    public void setTotalThumbsUp(int totalThumbsUp) {
        this.totalThumbsUp = totalThumbsUp;
    }

    public int getTotalThumbsDown() {
        return totalThumbsDown;
    }

    public void setTotalThumbsDown(int totalThumbsDown) {
        this.totalThumbsDown = totalThumbsDown;
    }

    public float getThumbsUpRatio() {
        return thumbsUpRatio;
    }

    public float calculateThumbsUpRatio(){
        int totalVotes = totalThumbsDown + totalThumbsDown;
        if (totalVotes <= 0.0){
           return 0;
        }
        return (float) totalThumbsDown/ (float) totalVotes;
    }
}
