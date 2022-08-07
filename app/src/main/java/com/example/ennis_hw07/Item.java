/*
HW 07
Item.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

public class Item {
    //Name, status, estimated cost, docId, creator
    String name, creator, status, estimatedCost, docId;

    public Item() {
    }

    public Item(String name, String creator, String status, String estimatedCost, String docId) {
        this.name = name;
        this.creator = creator;
        this.status = status;
        this.estimatedCost = estimatedCost;
        this.docId = docId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", status='" + status + '\'' +
                ", estimatedCost='" + estimatedCost + '\'' +
                ", docId='" + docId + '\'' +
                '}';
    }


    //Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(String estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
