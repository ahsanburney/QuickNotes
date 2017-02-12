package com.ahsanburney.quicknotes;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;



public class Data_Saved {

    private String dateTime;
    private String notes;

    public Data_Saved(){
        dateTime = "";
        notes = "";
    }

    public String getdateTime() {
        return dateTime;
    }

    public void setdateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getnotes() {
        return notes;
    }

    public void setnotes(String notes) {
        this.notes = notes;
    }

    public String toString() {
        return dateTime + ": " + notes;
    }

}