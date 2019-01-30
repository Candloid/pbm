package com.concordia.personalBudgetManager;

import java.time.LocalDate;

public class ExpenseRecord {
	public enum expenseType{Purchase,Bill};
	public enum paymentStatus{Paid,Unpaid};
	public enum operationType{Deposit,Withdraw,Modify,Delete};
	public enum statusDescrpition{paidByCash,paidByDebit,dueByCredit};
	public enum repetitionInterval{Day,Bidaily,Weekly,Biweekly,Monthly,Yearly}
	private double amount;
	private boolean paid;
	private String retailorName, retailorLocation, otherDetails;
	private LocalDate operationDate;
	
	ExpenseRecord() {
		this(0.0,false,"Retailor Name","Retailor Location",LocalDate.now(),"");
	}
	
	ExpenseRecord(double amount, Boolean paid, String retailorName, String retailorLocation, LocalDate operationDate, String otherDetails) {
		this.amount = amount;
		this.paid = paid;
		this.retailorName = retailorName;
		this.retailorLocation = retailorLocation;
		this.operationDate = operationDate;
		this.otherDetails = otherDetails;
	}
		
	public double getAmount() {return this.amount;}
	public boolean getPaid() {return this.paid;}
	public String getRetailorName(){return this.retailorName;}
	public String getRetailorLocation(){return this.retailorLocation;}
	public LocalDate getOperationDate(){return this.operationDate;}
	public String getOtherDetails(){return this.otherDetails;}
	
	public void setAmount(double amount) {this.amount=amount;}
	public void setPaid(boolean paid) {this.paid=paid;}
	public void setRetailorName(String retailorName){this.retailorName=retailorName;}
	public void setRetailorLocation(String retailorLocation){this.retailorLocation=retailorLocation;}
	public void setOperationDate(LocalDate operationDate){this.operationDate=operationDate;}	
	public void setOtherDetails(String otherDetails){this.otherDetails=otherDetails;}
}