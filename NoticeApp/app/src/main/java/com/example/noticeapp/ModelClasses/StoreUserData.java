package com.example.noticeapp.ModelClasses;

public class StoreUserData {
    String username, userPhone, userEmail;

    public StoreUserData(String username, String userPhone, String userEmail) {
        this.username = username;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
    }

    public StoreUserData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
