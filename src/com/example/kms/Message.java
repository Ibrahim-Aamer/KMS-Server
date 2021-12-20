package com.example.kms;

import java.io.Serializable;

// must implement Serializable in order to be sent
public class Message implements Serializable{
    private String text;
    private int id;
    private Employee emp;

    public Message(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setEmployee(Employee emp)
    {
        this.emp = emp;
    }

    public Employee getEmployee()
    {
        return emp;
    }

    public void setText(int i, String str)
    {
        this.id = i;
        this.text = str;
    }

    public String getText() {
        return (id+" "+text);
    }
}