package com.example.trialcheck;
public class UserHelperClass {
    String name, username, phoneNo, password;
    public UserHelperClass(String name, String username,  String phoneNo, String password) {
        this.name = name;
        this.username = username;

        this.phoneNo = phoneNo;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}