package com.concordia.personalBudgetManager;

import java.util.ArrayList;

public class User {
	String name="SomeUser";
	
	ArrayList<ExpenseRecord> records = new ArrayList<ExpenseRecord>();
	
	User(){
		ExpenseRecord zeroRecord = new ExpenseRecord();
		records.add(zeroRecord);
	}
	
	User(String name){
		this();
		this.name = name;
	}

    void addExpense(ExpenseRecord recordID){
    	records.add(recordID);
    }
    
    void removeExpense(ExpenseRecord recordID){
    	records.remove(recordID);
    }

    void modifyExpense(int recordID, ExpenseRecord newRecord){
    	ExpenseRecord oldRecord = records.get(recordID);
    	oldRecord.setAmount(newRecord.getAmount());
    	oldRecord.setOperationDate(newRecord.getOperationDate());
    	oldRecord.setOtherDetails(newRecord.getOtherDetails());
    	oldRecord.setPaid(newRecord.getPaid());
    	oldRecord.setRetailerLocation(newRecord.getRetailerLocation());
    	oldRecord.setRetailerName(newRecord.getRetailerName());
    }
}
