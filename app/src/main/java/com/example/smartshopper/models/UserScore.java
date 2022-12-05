package com.example.smartshopper.models;

public class UserScore {
    float totalThumbsUp;
    float totalThumbsDown;
    float thumbsUpRatio;

    public UserScore() {
    }

    public UserScore(float totalThumbsUp, float totalThumbsDown){
        this.totalThumbsUp = totalThumbsUp;
        this.totalThumbsDown = totalThumbsDown;
        this.thumbsUpRatio = 0.0f;
    }

    public float getTotalThumbsUp() {
        return totalThumbsUp;
    }

    public void setTotalThumbsUp(int totalThumbsUp) {
        this.totalThumbsUp = totalThumbsUp;
    }

    public float getTotalThumbsDown() {
        return totalThumbsDown;
    }

    public void setTotalThumbsDown(int totalThumbsDown) {
        this.totalThumbsDown = totalThumbsDown;
    }

    public float getThumbsUpRatio() {
        return thumbsUpRatio;
    }

    public void incrementUpVote(){
        this.totalThumbsUp += 1;
    }

    public void incrementDownVote(){
        this.totalThumbsDown += 1;
    }

    public void decrementUpVote(){
        this.totalThumbsUp -= 1;
    }

    public void decrementDownVote(){
        this.totalThumbsDown -= 1;
    }

    public void calculateThumbsUpRatio(){
        float totalVotes = totalThumbsDown + totalThumbsUp;
        if (totalVotes > 0.0){
            this.thumbsUpRatio = totalThumbsUp/ totalVotes;
        }
    }
}
