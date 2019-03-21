package com.concordia.personalBudgetManager;

import java.time.LocalDate;
import java.util.ArrayList;

public class ExpenseRecord {
	public enum expenseTypeE{Purchase,Bill,Composite};
	public enum paymentTypeE{paidByCash,paidByDebit,dueByCredit};
	public enum repetitionIntervalE{Once,Day,Bidaily,Weekly,Biweekly,Monthly,Yearly};
	public enum recordFieldE					{amount,	paid,	paidDate,		expenseType,			paymentType,				repetitionInterval,		retailerName,	retailerLocation,	operationDate,		otherDetails};
	public static String[] recordFieldStrings =	{"Amount",	"Paid",	"Paid Date",	"Expense Type",			"Payment Type",				"Repetition Interval",	"Retailer Name","Retailer Location","Operation Date",	"Other Details"};
	
	private expenseTypeE expenseType; 
	private paymentTypeE paymentType;
	private repetitionIntervalE repetitionInterval;

	private double amount;
	private boolean paid;
	private String retailerName, retailerLocation, otherDetails;
	private LocalDate operationDate, paidDate;
	private ArrayList<ExpenseRecord> subRecords = new ArrayList<ExpenseRecord>();
	
	public ExpenseRecord() {}
	
	public ExpenseRecord(double amount, Boolean paid, LocalDate paidDate, expenseTypeE expenseType, paymentTypeE paymentType,
						 repetitionIntervalE repetitionInterval, String retailerName, String retailerLocation, LocalDate operationDate, String otherDetails) {
		this.amount = amount;
		this.paid = paid;
		this.paidDate = operationDate;
		this.expenseType = expenseType;
		this.paymentType = paymentType;
		this.repetitionInterval = repetitionInterval;
		this.retailerName = retailerName;
		this.retailerLocation = retailerLocation;
		this.operationDate = operationDate;
		this.otherDetails = otherDetails;
		Object[] recordBody = {this.amount, this.paid, this.paidDate, this.expenseType, this.paymentType, this.repetitionInterval,
				this.retailerName, this.retailerLocation, this.operationDate, this.otherDetails};
	}
	
	public ExpenseRecord(Object[] record) {setByRecord(record);}
	
	private void setByRecord(Object[] record) {
		this.amount =				(double) record[recordFieldE.amount.ordinal()];
		this.paid =					(boolean) record[recordFieldE.paid.ordinal()];
		this.paidDate =				(LocalDate) record[recordFieldE.paidDate.ordinal()];
		this.expenseType =			(expenseTypeE) record[recordFieldE.expenseType.ordinal()];
		this.paymentType =			(paymentTypeE) record[recordFieldE.paymentType.ordinal()];
		this.repetitionInterval =	(repetitionIntervalE) record[recordFieldE.repetitionInterval.ordinal()];
		this.retailerName =			(String) record[recordFieldE.retailerName.ordinal()];
		this.retailerLocation =		(String) record[recordFieldE.retailerLocation.ordinal()];
		this.operationDate =		(LocalDate) record[recordFieldE.operationDate.ordinal()];
		this.otherDetails =			(String) record[recordFieldE.otherDetails.ordinal()];
	}

	public double getAmount() {return this.amount;}
	public boolean getPaid() {return this.paid;}
	public LocalDate getPaidDate() {return this.paidDate;}
	public expenseTypeE getExpenseType(){return this.expenseType;}
	public paymentTypeE getPaymentType(){return this.paymentType;}
	public repetitionIntervalE getRepetitionInterval(){return this.repetitionInterval;}
	public String getRetailerName(){return this.retailerName;}
	public String getRetailerLocation(){return this.retailerLocation;}
	public LocalDate getOperationDate(){return this.operationDate;}
	public String getOtherDetails(){return this.otherDetails;}
	public Object[] getRecord() {
		Object[] recordBody = {this.amount, this.paid, this.paidDate, this.expenseType, this.paymentType, this.repetitionInterval,
				this.retailerName, this.retailerLocation, this.operationDate, this.otherDetails};
		return recordBody;
	}
	public ExpenseRecord getSubRecord(int recordId) {return this.subRecords.get(recordId);}
	public int getSubRecordsCount() {return this.subRecords.size();}
	
	public void setAmount(double amount) {this.amount=amount;}
	public void setPaid(boolean paid) {this.paid=paid;}
	public void setPaidDate(LocalDate paidDate) {this.paidDate=paidDate;}
	public void setExpenseType(expenseTypeE expenseType){this.expenseType=expenseType;}
	public void setpaymentType(paymentTypeE paymentType){this.paymentType=paymentType;}
	public void setRepetitionInterval(repetitionIntervalE repetitionInterval){this.repetitionInterval=repetitionInterval;}
	public void setRetailerName(String retailerName){this.retailerName=retailerName;}
	public void setRetailerLocation(String retailerLocation){this.retailerLocation=retailerLocation;}
	public void setOperationDate(LocalDate operationDate){this.operationDate=operationDate;}	
	public void setOtherDetails(String otherDetails){this.otherDetails=otherDetails;}
	public void setRecord(Object[] record) {setByRecord(record);}
	public void insertSubRecord(int recordId, Object[] someRecord) {this.subRecords.add(recordId, new ExpenseRecord(someRecord));validateMaster();}
	public void insertSubRecord(int recordId, ExpenseRecord someRecord) {this.subRecords.add(recordId, someRecord);validateMaster();}
	public void addSubRecord(Object[] someRecord) {this.subRecords.add(new ExpenseRecord(someRecord));validateMaster();}
	public void addSubRecord(ExpenseRecord someRecord) {this.subRecords.add(someRecord);validateMaster();}
	public void removeSubRecord(int recordId) {this.subRecords.remove(recordId);validateMaster();}	
	public void validateMaster() {
		double sumAmount=0.0; boolean sumPaid=true;
		for(int i=0; i<this.getSubRecordsCount(); i++) {
			sumAmount += subRecords.get(i).amount;
			sumPaid &= subRecords.get(i).paid;
		}
		this.amount=sumAmount;
		this.paid=sumPaid;
	}
}