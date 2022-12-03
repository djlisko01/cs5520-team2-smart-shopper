package com.example.smartshopper.models;

public class DealDistance implements Comparable<DealDistance> {
    float distance;
    Deal deal;

    public DealDistance(float distance, Deal deal) {
        this.distance = distance;
        this.deal = deal;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    @Override
    public int compareTo(DealDistance o) {
        // if current object is greater,then return -1
        return Float.compare(this.distance, o.distance);
    }
}
