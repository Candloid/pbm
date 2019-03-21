package com.concordia.personalBudgetManager;

import java.time.LocalDate;
import java.util.ArrayList;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

public class User {
	String name="Guest";
	ArrayList<ExpenseRecord> records = new ArrayList<ExpenseRecord>();
	
	public User(String name){loadInitialRecords(); setName(name);}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}

	private void loadInitialRecords() { //Vlad - loading expense records - uncomment and change after merge with 2-level version
		ExpenseRecord [] preloadedRecords = new ExpenseRecord [6];
		for (int i = 0; i < preloadedRecords.length; i++) {
			preloadedRecords[i] = new ExpenseRecord(1000 * i + 1, i % 2 == 0 ? true : false, LocalDate.now(), expenseTypeE.values()[i % expenseTypeE.values().length],
					paymentTypeE.dueByCredit, repetitionIntervalE.values()[i % repetitionIntervalE.values().length], "Retailer " + i, "Location " + i, LocalDate.now(), "");
			if(preloadedRecords[i].getExpenseType().equals(expenseTypeE.Composite)) {
				for (int j = 0; j < 6; j++) {
					preloadedRecords[i].addSubRecord(new ExpenseRecord(1000 * j + 1, j % 2 == 0 ? true : false, LocalDate.now(),
							expenseTypeE.values()[j % (expenseTypeE.values().length-1)],paymentTypeE.values()[j % paymentTypeE.values().length],
							repetitionIntervalE.values()[j % repetitionIntervalE.values().length], "Retailer " + j, "Location " + j, LocalDate.now(), "Sub of " + i));
				}
			}
			preloadedRecords[0].setpaymentType(paymentTypeE.paidByCash);
			records.add(preloadedRecords[i]);
		}
	} //end of loading expense records
}