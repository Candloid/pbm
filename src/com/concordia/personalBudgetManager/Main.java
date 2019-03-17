package com.concordia.personalBudgetManager;

public class Main {
    public static void main(String[] args) {
    	User currentUser = new User("Jane Doe");
		RecordManagementForm editorUserForm = new RecordManagementForm(currentUser);
    	editorUserForm.loadForm(currentUser);
    }
}