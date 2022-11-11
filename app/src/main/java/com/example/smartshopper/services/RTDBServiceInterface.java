package com.example.smartshopper.services;

import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.ObjectInterface;
import com.google.firebase.database.Query;

public interface RTDBServiceInterface {
    // Define Queries here
    Query getAll(String child);

    Query getDealsBySearch(String search);

    Query getUser(String username);

    Query getDeal(String dealID);

    void writeUser(User user);

    void writeDeal(Deal deal);

    // Use this to determine whether item exists (bool) for particular query
    void getStatusResponseFromQuery(Query query, BoolInterface boolInterface);

    // Use this to get a single response (object) from a query, may contain children
    void getDataResponseFromQuery(Query query, ObjectInterface objectInterface);

    // TODO: Write another method to get multiple json objects from a query?
    // Not sure if we can use method above for multiple results
}
