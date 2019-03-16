package com.concordia.personalBudgetManager;

import java.time.LocalDate;
import java.util.ArrayList;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

public class User {
	String name="Guest";
	
	ArrayList<ExpenseRecord> records = new ArrayList<ExpenseRecord>();
	
	User(){
    	loadRecords();
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
    
    private void loadRecords() {
    	//Vlad - loading expense records - uncomment and change after merge with 2-level version
    	ExpenseRecord [] preloadedRecords = new ExpenseRecord [5];
    	for (int i = 0; i < preloadedRecords.length; i++) {
    		preloadedRecords[i] = new ExpenseRecord(1000 * i + 1, i%2 == 0 ? true : false, LocalDate.now(), expenseTypeE.Purchase,
    				paymentTypeE.dueByCredit, repetitionIntervalE.Once, "Retailer " + i, "Location " + i,
    				LocalDate.now(), "");
    		records.add(preloadedRecords[i]);
    	}
    	//end of loading expense records
    }
}
