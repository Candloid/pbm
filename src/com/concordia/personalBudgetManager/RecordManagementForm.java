package com.concordia.personalBudgetManager;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.JButton;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

import com.concordia.personalBudgetManager.ExpenseRecord.*;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DateEditor;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class RecordManagementForm {
	
	int oldRecordId = -1, currentRecordId, ghostRecordId;
	boolean firstRun = true;

	private JFrame frame;
	
	private JSpinner IdSpinner;
	private JCheckBox paidTick;
	private expenseTypeE expenseType;
	private JComboBox<expenseTypeE> typeSelection;	
	private JComboBox<repetitionIntervalE> repetitionIntervalSelection;
	private JTextField retailerNameText;
	private JTextField retailerLocationText;
	private JTextField amountText;
	private JTextField operationDateText;
	private JTextField otherDetailsText;

	
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
		IdSpinner.setValue(currentUser.records.size());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize(User currentUser) {
		frame = new JFrame();
		frame.setTitle("[" + currentUser.name + "] expense records");
		frame.setBounds(100, 100, 360, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container p = frame.getContentPane();
		p.setLayout(null);
		
		JLabel l1 = new JLabel("Expense ID");
		l1.setBounds(25, 32, 78, 25);
		frame.getContentPane().add(l1);
		
		JLabel l2 = new JLabel("Expense type:");
		l2.setBounds(25, 89, 130, 25);
		frame.getContentPane().add(l2);
		
		JLabel l3 = new JLabel("Retailer name");
		l3.setBounds(25, 146, 130, 25);
		frame.getContentPane().add(l3);
		
		JLabel l4 = new JLabel("Retailer location");
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
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentUser.records.add(new ExpenseRecord());
				IdSpinner.setValue(currentUser.records.size());
			}
		});
		btnInsert.setBounds(18, 485, 150, 25);
		frame.getContentPane().add(btnInsert);

		operationDateText = new JTextField(LocalDate.now().toString());
		frame.getContentPane().add(operationDateText);
		
		IdSpinner = new JSpinner();
		IdSpinner.setValue(Integer.parseInt("0"));
		IdSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int nowAt = (int)IdSpinner.getValue(); 
				if (nowAt < currentUser.records.size() & nowAt > -1) {					
					currentRecordId = (int)IdSpinner.getValue();
					if (oldRecordId!=currentRecordId & oldRecordId!=-1) {
						//JOptionPane.showMessageDialog(frame, "was:" + oldRecordId + ", now:" + currentRecordId, "old and new?", JOptionPane.OK_CANCEL_OPTION);
						ExpenseRecord oldRecord = currentUser.records.get(oldRecordId);
						if(sniffChanges(oldRecord)) {
							discardRecordsConfirmation(oldRecord);
						}
					}
					oldRecordId=currentRecordId;
					
					ExpenseRecord record = currentUser.records.get((int) IdSpinner.getValue());
					updateFields(record);
				} else {
					if (nowAt<0) {
						ExpenseRecord zerothRecord = currentUser.records.get(0);
						if (oldRecordId == 0 & sniffChanges(zerothRecord) & !firstRun) {
							discardRecordsConfirmation(currentUser.records.get(0));
							firstRun = false;
						}
						IdSpinner.setValue(0);
					} else {
						int lastRecordId = currentUser.records.size()-1;
						ExpenseRecord lastRecord = currentUser.records.get(lastRecordId);
						if (oldRecordId == lastRecordId & sniffChanges(lastRecord) & !firstRun)
							discardRecordsConfirmation(currentUser.records.get(lastRecordId));
						IdSpinner.setValue(lastRecordId);
					}
				}
			}
		});
		IdSpinner.setBounds(165, 32, 150, 25);
		frame.getContentPane().add(IdSpinner);
		
		typeSelection = new JComboBox<expenseTypeE>();
		typeSelection.setModel(new DefaultComboBoxModel<expenseTypeE>(expenseTypeE.values()));
		typeSelection.setBounds(165, 89, 150, 25);
		frame.getContentPane().add(typeSelection);
		
		retailerLocationText = new JTextField(10);
		retailerLocationText.setBounds(165, 203, 150, 25);
		frame.getContentPane().add(retailerLocationText);
		
		retailerNameText = new JTextField(10);
		retailerNameText.setBounds(165, 146, 150, 25);
		frame.getContentPane().add(retailerNameText);
		
		repetitionIntervalSelection = new JComboBox<repetitionIntervalE>();
		repetitionIntervalSelection.setModel(new DefaultComboBoxModel<repetitionIntervalE>(repetitionIntervalE.values()));
		repetitionIntervalSelection.setBounds(165, 431, 150, 25);
		frame.getContentPane().add(repetitionIntervalSelection);

		JSpinner timeSpinnerText = new JSpinner(new SpinnerDateModel());
		DateEditor timeSpinnerTextSelection = new JSpinner.DateEditor(timeSpinnerText, "yyyy-MM-dd HH:mm:ss");
		timeSpinnerText.setBounds(165, 317, 150, 25);
		timeSpinnerText.setEditor(timeSpinnerTextSelection);
		frame.getContentPane().add(timeSpinnerText);
		
		amountText = new JTextField(10);
		amountText.setBounds(165, 260, 150, 25);
		frame.getContentPane().add(amountText);
		
		otherDetailsText = new JTextField();
		otherDetailsText.setBounds(165, 374, 150, 25);
		frame.getContentPane().add(otherDetailsText);
		
		JButton btnDelete = new JButton("Delete Record");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int totalCount = currentUser.records.size();
				if (totalCount>1) {
					int id = (int) IdSpinner.getValue();
					IdSpinner.setValue(id-1);
					currentUser.records.remove(id);
				} else {
					JOptionPane.showMessageDialog(frame, "The last and only record cannot be deleted","Invalid Operation",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDelete.setBounds(180, 485, 150, 25);
		frame.getContentPane().add(btnDelete);
		
		
		JButton btnSave = new JButton("Apply changes");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpenseRecord record = currentUser.records.get((int) IdSpinner.getValue());
				applyChanges(record);
			}
		});
		btnSave.setBounds(18, 516, 150, 25);
		frame.getContentPane().add(btnSave);
		
		JButton btnDiscard = new JButton("Discard changes");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExpenseRecord record = currentUser.records.get((int) IdSpinner.getValue());
				discardRecordsConfirmation(record);
			}
		});
		btnDiscard.setBounds(180, 516, 150, 25);
		frame.getContentPane().add(btnDiscard);
		
		paidTick = new JCheckBox("Paid");
		paidTick.setBounds(112, 89, 50, 24);
		frame.getContentPane().add(paidTick);
		frame.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{IdSpinner, typeSelection, retailerNameText, retailerLocationText, amountText, otherDetailsText, repetitionIntervalSelection, operationDateText, btnInsert, l1, l2, l4, l3, l9, l5, l6}));
	}
	
	void discardRecordsConfirmation(ExpenseRecord record) {
	    Object[] options = {"Discard Changes","Apply Changes"};
	    int confirmation = JOptionPane.showOptionDialog(frame, "Are you sure?", "Discard Changes?", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	    if(confirmation==0)
	    	updateFields(record);
	    if(confirmation==1)
	    	applyChanges(record);
	}

	void updateFields(ExpenseRecord record) {
		amountText.setText(Double.toString(record.getAmount()));
		paidTick.setSelected(record.getPaid());
		expenseType = record.getExpenseType();
		typeSelection.setSelectedItem(expenseType);
		repetitionIntervalSelection.setSelectedItem((repetitionIntervalE)record.getRepetitionInterval());					
		retailerNameText.setText(record.getRetailerName());
		retailerLocationText.setText(record.getRetailerLocation());
		operationDateText.setText(record.getOperationDate().toString());
		otherDetailsText.setText(record.getOtherDetails());
	}

	void applyChanges(ExpenseRecord record) {
		record.setAmount(Double.parseDouble(amountText.getText()));
		record.setPaid(paidTick.isSelected());
		record.setPaymentStatus(paymentStatusE.values()[paidTick.isSelected() ? 1 : 0 ]);
		record.setExpenseType((expenseTypeE) typeSelection.getSelectedItem());
		record.setRepetitionInterval((repetitionIntervalE) repetitionIntervalSelection.getSelectedItem());
		record.setRetailerName(retailerNameText.getText());
		record.setRetailerLocation(retailerLocationText.getText());
		record.setOperationDate(LocalDate.parse(operationDateText.getText()));
		record.setOtherDetails(otherDetailsText.getText());
	}
	
	boolean sniffChanges(ExpenseRecord record) {
		return !(
		(Double.toString(record.getAmount()).equals(amountText.getText())) &
		(record.getExpenseType() == (expenseTypeE) typeSelection.getSelectedItem()) &
		(record.getOperationDate().toString().equals(LocalDate.parse(operationDateText.getText()).toString())) &
		(record.getOtherDetails().equals(otherDetailsText.getText())) &
		(record.getPaid() == paidTick.isSelected()) &
		(record.getPaymentStatus() == paymentStatusE.values()[paidTick.isSelected() ? 1 : 0 ]) &
		(record.getRepetitionInterval() == (repetitionIntervalE) repetitionIntervalSelection.getSelectedItem()) &
		(record.getRetailerLocation().equals(retailerLocationText.getText())) &
		(record.getRetailerName().equals(retailerNameText.getText()))
		);
	}
}