/*
HW 07
User.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import java.util.ArrayList;

public class User {
    //name and userId, docId (?)
    String name, userId, docId;
    ArrayList<String> friendsList;

    public User() {
    }

    public User(String name, String userId, String docId, ArrayList<String> friendsList) {
        this.name = name;
        this.userId = userId;
        this.docId = docId;
        this.friendsList = friendsList;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", docId='" + docId + '\'' +
                ", friendsList=" + friendsList +
                '}';
    }


    //Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public ArrayList<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(ArrayList<String> friendsList) {
        this.friendsList = friendsList;
    }
}
