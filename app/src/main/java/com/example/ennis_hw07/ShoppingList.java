/*
HW 07
ShoppingList.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingList implements Serializable {

    String title, creator, docId, ownerId;
    //Array of users invited to the shopping list
    ArrayList<String> usersInvited;

    public ShoppingList() {
    }

    public ShoppingList(String title, String creator, String docId, String ownerId, ArrayList<String> usersInvited) {
        this.title = title;
        this.creator = creator;
        this.docId = docId;
        this.ownerId = ownerId;
        this.usersInvited = usersInvited;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "title='" + title + '\'' +
                ", creator='" + creator + '\'' +
                ", docId='" + docId + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", usersInvited=" + usersInvited +
                '}';
    }


    //Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public ArrayList<String> getUsersInvited() {
        return usersInvited;
    }

    public void setUsersInvited(ArrayList<String> usersInvited) {
        this.usersInvited = usersInvited;
    }
}
