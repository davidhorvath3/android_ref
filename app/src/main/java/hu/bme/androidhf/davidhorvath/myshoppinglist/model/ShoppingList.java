package hu.bme.androidhf.davidhorvath.myshoppinglist.model;

import java.util.Date;

/**
 * Created by david on 2017.10.19..
 */

public class ShoppingList {

    private int id;
    private String listName;
    private String    deadline;

    public ShoppingList(){}

    public ShoppingList(int id, String listName,String date) {
        this.id = id;
        this.listName = listName;
        this.deadline = date;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
