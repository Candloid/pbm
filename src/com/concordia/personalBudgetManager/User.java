package com.concordia.personalBudgetManager;

import java.util.ArrayList;

public class User {
	String name="Guest";
	
	ArrayList<ExpenseRecord> records = new ArrayList<ExpenseRecord>();
	
	User(){
	}
	
	public User(String name){
		this();
		this.name = name;
	}

    public String getName() {
        return name;
    }

    public ArrayList<ExpenseRecord> getRecords() {
        return records;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecords(ArrayList<ExpenseRecord> records) {
        this.records = records;
    }
}
