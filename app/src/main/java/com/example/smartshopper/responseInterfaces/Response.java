package com.example.smartshopper.responseInterfaces;

import com.example.smartshopper.models.Deal;

import java.util.List;

public abstract class Response {
    public abstract void onCallback(Object response);

    public abstract void onCallback(boolean response);

    public abstract void onCallback(Deal response);

    public abstract void onCallback(int response);

    public abstract void onCallback(double response);

    public abstract void onCallback(float response);

    public abstract void onCallback(long response);

    public abstract void onCallback(List response);

}
