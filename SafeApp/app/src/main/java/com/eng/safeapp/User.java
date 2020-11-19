package com.eng.safeapp;

import java.util.ArrayList;

public class User {
    public String fullName, email;
    public ArrayList<Integer> facialExpressionScoreHistory;

    public User() {}

    public User(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
        this.facialExpressionScoreHistory = new ArrayList<>();
    }
}
