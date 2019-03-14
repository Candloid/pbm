package com.concordia.personalBudgetManager;

import java.time.LocalDate;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

public class Main {
    public static void main(String[] args) {
	// write your code here
    	User currentUser = new User("Jane Doe");
    	
    	/*
    	//loading expense records
		ExpenseRecord [] preloadedRecords = new ExpenseRecord [5];
		for (int i = 0; i < preloadedRecords.length; i++) {
			preloadedRecords[i] = new ExpenseRecord(1000 * i + 1, false, LocalDate.now(), expenseTypeE.Purchase,
					paymentTypeE.dueByCredit, repetitionIntervalE.Once, "Retailer " + i, "Location " + i,
					LocalDate.now(), "");
			currentUser.records.add(preloadedRecords[i]);
		}
		//end of loading expense records
		*/
		
		RecordManagementForm editorUserForm = new RecordManagementForm(currentUser);
    	editorUserForm.loadForm(currentUser);
    }
}