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
}
