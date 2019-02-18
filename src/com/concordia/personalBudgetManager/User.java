package com.concordia.personalBudgetManager;

import java.util.ArrayList;

public class User {
	String name="Guest";
	
	ArrayList<ExpenseRecord> records = new ArrayList<ExpenseRecord>();
	
	User(){
		/*
		ExpenseRecord zeroRecord = new ExpenseRecord();
		records.add(zeroRecord);
		*/
	}
	
	User(String name){
		this();
		this.name = name;
	}
}
