package com.concordia.personalBudgetManager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JButton;
import java.awt.Container;

import javax.swing.JLabel;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

import com.concordia.personalBudgetManager.ExpenseRecord.*;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

public class RecordManagementForm {

	private JFrame frame;
	private JSpinner IdText;
	private JComboBox<Object> typeText;
	private JTextField retailorLocationText;
	private JTextField retailorNameText;
	private JComboBox<Object> repetitionIntervalText;
	private JTextField amountText;
	
	/**
	 * Launch the application.
	 */
	public void loadForm(User currentUser) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecordManagementForm window = new RecordManagementForm(currentUser);
					window.frame.setVisible(true);
					frame.getContentPane().setLayout(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RecordManagementForm(User currentUser) {
		initialize(currentUser);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize(User currentUser) {
		frame = new JFrame();
		frame.setTitle("[" + currentUser.name + "] expense records");
		frame.setBounds(100, 100, 360, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/*
		ExpenseRecord baseRecord = currentUser.records.get(0);
		String retailorName = baseRecord.getRetailorName(); 
		String retailorLocation = baseRecord.getRetailorLocation();
		repetitionInterval repetitionIntervalSelection;
		paymentStatus paymentStatusSelection;
		double amount = baseRecord.getAmount();
		LocalDate operationDate = baseRecord.getOperationDate();
		*/
		
		Container p = frame.getContentPane();
		p.setLayout(null);
		
		JLabel l1 = new JLabel("Expense ID");
		l1.setBounds(25, 32, 78, 25);
		frame.getContentPane().add(l1);
		
		JLabel l2 = new JLabel("Expense type:");
		l2.setBounds(25, 89, 130, 25);
		frame.getContentPane().add(l2);
		
		JLabel l3 = new JLabel("retailor name");
		l3.setBounds(25, 146, 130, 25);
		frame.getContentPane().add(l3);
		
		JLabel l4 = new JLabel("retailor location");
		l4.setBounds(25, 203, 130, 25);
		frame.getContentPane().add(l4);
		
		JLabel l5 = new JLabel("Amount");
		l5.setBounds(25, 260, 130, 25);
		frame.getContentPane().add(l5);
		
		JLabel l6 = new JLabel("Date");
		l6.setBounds(25, 317, 130, 25);
		frame.getContentPane().add(l6);
		
		JLabel l7 = new JLabel("Other details");
		l7.setBounds(25, 374, 130, 25);
		frame.getContentPane().add(l7);
		
		JLabel l9 = new JLabel("Repetition interval");
		l9.setBounds(25, 431, 130, 25);
		frame.getContentPane().add(l9);
		
		JButton btnInsert = new JButton("Insert Record");
		btnInsert.setBounds(18, 485, 150, 25);
		frame.getContentPane().add(btnInsert);

		JTextField operationDateText = new JTextField();
		frame.getContentPane().add(operationDateText);
		
		IdText = new JSpinner();
		IdText.setBounds(165, 32, 150, 25);
		//SpinnerNumberModel limits = new SpinnerNumberModel(5.0, 0, currentUser.records, 1.0);  
		//new SpinnerNumberModel(Default, Min, Max, Step);  
		//JSpinner spin1 = new JSpinner(limits);
		frame.getContentPane().add(IdText);
		
		typeText = new JComboBox<Object>();
		typeText.setModel(new DefaultComboBoxModel<Object>(expenseType.values()));
		typeText.setBounds(165, 89, 150, 25);
		frame.getContentPane().add(typeText);
		
		retailorLocationText = new JTextField();
		retailorLocationText.setColumns(10);
		retailorLocationText.setBounds(165, 203, 150, 25);
		frame.getContentPane().add(retailorLocationText);
		
		retailorNameText = new JTextField();
		retailorNameText.setColumns(10);
		retailorNameText.setBounds(165, 146, 150, 25);
		frame.getContentPane().add(retailorNameText);
		//retailorName = retailorNameText.getText();
		
		repetitionIntervalText = new JComboBox<Object>();
		repetitionIntervalText.setModel(new DefaultComboBoxModel<Object>(repetitionInterval.values()));
		repetitionIntervalText.setBounds(165, 431, 150, 25);
		frame.getContentPane().add(repetitionIntervalText);
		//paymentStatusSelection = (paymentStatus) statusText.getSelectedItem();

		JSpinner timeSpinnerText = new JSpinner(new SpinnerDateModel());
		DateEditor timeSpinnerTextSelection = new JSpinner.DateEditor(timeSpinnerText, "yyyy-MM-dd HH:mm:ss");
		timeSpinnerText.setBounds(165, 317, 150, 25);
		timeSpinnerText.setEditor(timeSpinnerTextSelection);
		//timeSpinnerText.setValue(LocalDate.now());
		frame.getContentPane().add(timeSpinnerText);
		
		amountText = new JTextField();
		amountText.setColumns(10);
		amountText.setBounds(165, 260, 150, 25);
		frame.getContentPane().add(amountText);
		
		JTextField detailsText = new JTextField();
		detailsText.setBounds(165, 374, 150, 25);
		frame.getContentPane().add(detailsText);
		
		JButton btnDelete = new JButton("Delete Record");
		btnDelete.setBounds(180, 485, 150, 25);
		frame.getContentPane().add(btnDelete);
		
		
		JButton btnSave = new JButton("Save changes");
		btnSave.setBounds(18, 516, 150, 25);
		frame.getContentPane().add(btnSave);
		
		JButton btnDiscard = new JButton("Discard changes");
		btnDiscard.setBounds(180, 516, 150, 25);
		frame.getContentPane().add(btnDiscard);
		
		JCheckBox paidTick = new JCheckBox("Paid");
		paidTick.setBounds(109, 32, 50, 24);
		frame.getContentPane().add(paidTick);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{IdText, typeText, retailorNameText, retailorLocationText, amountText, detailsText, repetitionIntervalText, operationDateText, btnInsert, l1, l2, l4, l3, l9, l5, l6}));
	}	
}