package com.ibm.mobilefirstplatform.clientsdk.andriod.push.model;

public enum UserType {

    NORMAL("Normal User"),
    COVID19USER("Covid-19 Affected User"),
    OFFICER("Public Servant");

    UserType(String userType) {
        this.userType = userType;
    }

    private String userType;

    public String getUserType() {
        return userType;
    }
}
