package com.concordia.personalBudgetManager;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


import java.awt.event.InputEvent;
import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import com.concordia.personalBudgetManager.*;
import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentStatusE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;
import com.concordia.personalBudgetManager.ExpenseRecord.statusDescriptionE;


class Test {
	public static User currentUser = new User("Jane Doe");
	public static RecordManagementForm editorUserForm = new RecordManagementForm(currentUser);
	
	@BeforeClass  
    public static void init() {  
    	editorUserForm.loadForm(currentUser);
    } 

	@Test
	void testUpdateFields() {
		
		ExpenseRecord myRecord = new ExpenseRecord(5.0, true, expenseTypeE.Purchase,paymentStatusE.Unpaid,statusDescriptionE.dueByCredit,
				repetitionIntervalE.Once,"Burger King","Retailer Location",LocalDate.now(),"Details");
		
		editorUserForm.updateFields(myRecord);
		
		String newNameLable = editorUserForm.retailerNameText.getText();
		String newAmountLable = editorUserForm.amountText.getText();
		
		assertEquals(newNameLable,myRecord.retailerName);
		assertEquals(newAmountLable,""+myRecord.amount);
	}

	@Test
	public void testApplyChanges() {
    	
		String myRetailer = "coco";
		double myAmount = 10;	
		ExpenseRecord myRecord = new ExpenseRecord(1.0, true, expenseTypeE.Purchase,paymentStatusE.Unpaid,statusDescriptionE.dueByCredit,
				repetitionIntervalE.Once,"Retailer Name","Retailer Location",LocalDate.now(),"Details");
		
		//mock texts
		editorUserForm.amountText.setText(""+myAmount);
		editorUserForm.retailerNameText.setText(myRetailer);
		// updateFields function
		editorUserForm.applyChanges(myRecord);
		System.out.println("After applying changes, the retailer name is: "+myRecord.retailerName);
		
		assertEquals(myRetailer,myRecord.retailerName);
	}

}
