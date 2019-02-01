package com.concordia.personalBudgetManager;

import java.time.LocalDate;

public class ExpenseRecord {
	public enum expenseTypeE{Purchase,Bill};
	public enum paymentStatusE{Unpaid,paid};
	public enum operationTypeE{Deposit,Withdraw,Modify,Delete};
	public enum statusDescriptionE{paidByCash,paidByDebit,dueByCredit};
	public enum repetitionIntervalE{Once,Day,Bidaily,Weekly,Biweekly,Monthly,Yearly};
	
	private expenseTypeE expenseType; 
	private paymentStatusE paymentStatus;
	private operationTypeE operationType;
	private statusDescriptionE statusDescrpition;
	private repetitionIntervalE repetitionInterval;

	private double amount;
	private boolean paid;
	private String retailerName, retailerLocation, otherDetails;
	private LocalDate operationDate;
	
	ExpenseRecord() {
		this(0.0,false,expenseTypeE.Purchase,paymentStatusE.Unpaid,statusDescriptionE.dueByCredit,
				repetitionIntervalE.Once,"Retailer Name","Retailer Location",LocalDate.now(),"Details");
	}
	
	ExpenseRecord(double amount, Boolean paid, expenseTypeE expenseType, paymentStatusE paymentStatus, statusDescriptionE statusDescription,
			repetitionIntervalE repetitionInterval, String retailerName, String retailerLocation, LocalDate operationDate, String otherDetails) {
		this.amount = amount;
		this.paid = paid;
		this.expenseType = expenseTypeE.Purchase;
		this.paymentStatus = paymentStatusE.Unpaid;
		this.statusDescrpition = statusDescriptionE.dueByCredit;
		this.repetitionInterval = repetitionIntervalE.Once;
		this.retailerName = retailerName;
		this.retailerLocation = retailerLocation;
		this.operationDate = operationDate;
		this.otherDetails = otherDetails;
	}
		
	public double getAmount() {return this.amount;}
	public boolean getPaid() {return this.paid;}
	public expenseTypeE getExpenseType(){return this.expenseType;}
	public paymentStatusE getPaymentStatus(){return this.paymentStatus;}
	public operationTypeE getOperationType(){return this.operationType;}
	public statusDescriptionE getStatusDescrpition(){return this.statusDescrpition;}
	public repetitionIntervalE getRepetitionInterval(){return this.repetitionInterval;}

	public String getRetailerName(){return this.retailerName;}
	public String getRetailerLocation(){return this.retailerLocation;}
	public LocalDate getOperationDate(){return this.operationDate;}
	public String getOtherDetails(){return this.otherDetails;}
	
	public void setAmount(double amount) {this.amount=amount;}
	public void setPaid(boolean paid) {this.paid=paid;}
	public void setExpenseType(expenseTypeE expenseType){this.expenseType=expenseType;}
	public void setPaymentStatus(paymentStatusE paymentStatus){this.paymentStatus=paymentStatus;}
	public void setOperationType(operationTypeE operationType){this.operationType=operationType;}
	/*public void setStatusDescrpition(statusDescriptionE statusDescription){this.statusDescrpition=statusDescrpition;}*/
	public void setRepetitionInterval(repetitionIntervalE repetitionInterval){this.repetitionInterval=repetitionInterval;}

	public void setRetailerName(String retailerName){this.retailerName=retailerName;}
	public void setRetailerLocation(String retailerLocation){this.retailerLocation=retailerLocation;}
	public void setOperationDate(LocalDate operationDate){this.operationDate=operationDate;}	
	public void setOtherDetails(String otherDetails){this.otherDetails=otherDetails;}
}