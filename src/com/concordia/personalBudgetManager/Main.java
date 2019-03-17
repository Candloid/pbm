package com.concordia.personalBudgetManager;
import com.concordia.personalBudgetManager.RecordManagementForm;

public class Main {
    public static void main(String[] args) {
    	User JaneDoe = new User("Jane Doe");
    	new RecordManagementForm(JaneDoe);
    }
}