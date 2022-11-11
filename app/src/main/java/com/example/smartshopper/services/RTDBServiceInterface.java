package com.example.smartshopper.services;

import com.example.smartshopper.projectModels.Deal;
import com.example.smartshopper.projectModels.User;
import com.example.smartshopper.responseInterfaces.BoolInterface;
import com.example.smartshopper.responseInterfaces.ObjectInterface;
import com.google.firebase.database.Query;

public interface RTDBServiceInterface {
    // Define Queries here

    // Get all users / deals / comments / etc
    Query getAll(String child);

    // Get deals by specific search term
    Query getDealsBySearch(String search);

    // Get user by username
    Query getUser(String username);

    // Get deal by dealID
    Query getDeal(String dealID);

    // Get all deals saved by user
    Query getSavedDeals(User user);

    // Get all deals posted by user
    Query getPostedDeals(User user);

    // Get all comments by user
    Query getComments(User user);

    // Get all friends by user
    Query getFriends(User user);

    // Write user to database
    void writeUser(User user);

    // Write deal to database
    void writeDeal(Deal deal);

    // Use this to determine whether item exists (bool) for particular query
    void getStatusResponseFromQuery(Query query, BoolInterface boolInterface);

    // Use this to get a single response (object) from a query, may contain children
    void getDataResponseFromQuery(Query query, ObjectInterface objectInterface);

    // TODO: Write another method to get multiple json objects from a query?
    // Not sure if we can use method above for multiple results
}