package com.ibm.mobilefirstplatform.clientsdk.andriod.push.model;

import android.icu.text.UnicodeSet;

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

    public static UserType getUserType(String type) {
        UserType selectedType = null;
        for (UserType userType : UserType.values()) {
            if (userType.getUserType().equalsIgnoreCase(type)) {
                selectedType = userType;
            }
        }
        return selectedType;
    }
}
