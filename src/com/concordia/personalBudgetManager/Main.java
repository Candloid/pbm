package com.concordia.personalBudgetManager;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

public class Main {
    public static void main(String[] args) {
    	User currentUser = new User("Jane Doe");
		RecordManagementForm editorUserForm = new RecordManagementForm(currentUser);
    	editorUserForm.loadForm(currentUser);
    }
}