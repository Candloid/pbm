package com.concordia.personalBudgetManager;

import com.concordia.personalBudgetManager.ExpenseRecord.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.Container;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.event.ChangeEvent;
import javax.swing.JScrollPane;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.swing.UIManager.*;

public class RecordManagementForm {
	
	private ExpenseRecord currentRecord, packedRecord;
	int currentRecordId, oldRecordId=0, toggleCounter=1;
	boolean firstRun = true, deleteInProgress = false;

	private RecordManagementForm window;
	private JFrame frame;
	private JTable mainTable;
	private DefaultTableModel model;

	private JSpinner idSpinner, paidDateSpinner, operationDateSpinner;
	private SpinnerNumberModel idRange;
	private int idMax = 0, idMin = 0;
	
	private JCheckBox paidTick;
	private JComboBox<expenseTypeE> expenseTypeSelection;
	private JComboBox<repetitionIntervalE> repetitionIntervalSelection;
	private JComboBox<paymentTypeE> paymentTypeSelection;
	private JTextField retailerNameText, retailerLocationText, amountText, otherDetailsText;
	
	private Container p;
	private JScrollPane scrollPane;
	private JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9;
	private JButton btnInsert, btnDelete, btnSave, btnDiscard;
	
	// Launch the application
	public void loadForm(User currentUser) {
		// Set the "look and feel" of the form as Nimbus
		this.setLookAndFeel("Nimbus");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new RecordManagementForm(currentUser);
					window.frame.setVisible(true);
					frame.getContentPane().setLayout(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	// Set the "look and feel" of the form
	private void setLookAndFeel(String themeName) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if (themeName.equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			System.out.println("DEBUG: ERROR - [" + themeName + "] theme is not available");
		    // If the given theme is not available, you can set the GUI to another look and feel.
		}
	}

	// Create the application.
	public RecordManagementForm(User currentUser) {
		initialize(currentUser);
		model = new DefaultTableModel(null, ExpenseRecord.recordFieldStrings) {
			/**
			 Makes the table read-only. 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		mainTable = new JTable(model);
		mainTable.setBounds(384, 32, 1186, 509);
		setUpColumnInputModels();	//Fiddle with the Sport column's cell editors/renderers.
		/*
		mainTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idSpinner.setValue(mainTable.getSelectedRow());
				updateFields(new ExpenseRecord(currentUser.records.get((int)idSpinner.getValue()).getRecord()));
			}
		});
		*/
		//mainTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int nowAt = mainTable.getSelectedRow();
				if(nowAt != -1)
					idSpinner.setValue(nowAt);
			}
		});

		scrollPane = new JScrollPane();
		scrollPane.setBounds(343, 32, 933, 430);
		frame.getContentPane().add(scrollPane);
		scrollPane.setViewportView(mainTable);
		
		amountText.setText("0.0");
		toggleButtons();
	}
	
	private void initialize(User currentUser) {
		frame = new JFrame();
		frame.setTitle("[" + currentUser.name + "] expense records");
		frame.setBounds(100, 100, 1300, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		p = frame.getContentPane();
		p.setLayout(null);
		
		l1 = new JLabel("Expense ID");
		l1.setBounds(25, 32, 78, 25);
		frame.getContentPane().add(l1);
		
		l2 = new JLabel("Payment type:");
		l2.setBounds(25, 105, 130, 25);
		frame.getContentPane().add(l2);
		
		l3 = new JLabel("Expense type:");
		l3.setBounds(25, 142, 130, 25);
		frame.getContentPane().add(l3);
		
		l4 = new JLabel("Retailer name");
		l4.setBounds(25, 179, 130, 25);
		frame.getContentPane().add(l4);
		
		l5 = new JLabel("Retailer location");
		l5.setBounds(25, 216, 130, 25);
		frame.getContentPane().add(l5);
		
		l6 = new JLabel("Amount");
		l6.setBounds(25, 253, 130, 25);
		frame.getContentPane().add(l6);
		
		l7 = new JLabel("Date");
		l7.setBounds(25, 290, 130, 25);
		frame.getContentPane().add(l7);
		
		l8 = new JLabel("Other details");
		l8.setBounds(25, 327, 130, 25);
		frame.getContentPane().add(l8);
		
		l9 = new JLabel("Repetition interval");
		l9.setBounds(25, 364, 130, 25);
		frame.getContentPane().add(l9);
		
		operationDateSpinner = new JSpinner(new SpinnerDateModel());
		operationDateSpinner.setEditor(new JSpinner.DateEditor(operationDateSpinner, "yyyy-MM-dd HH:mm:ss"));
		operationDateSpinner.setBounds(165, 289, 150, 25);
		frame.getContentPane().add(operationDateSpinner);
		
		paidDateSpinner = new JSpinner(new SpinnerDateModel());
		paidDateSpinner.setEditor(new JSpinner.DateEditor(paidDateSpinner, "yyyy-MM-dd HH:mm:ss"));
		paidDateSpinner.setBounds(165, 68, 150, 25);
		frame.getContentPane().add(paidDateSpinner);
		
		idRange = new SpinnerNumberModel(0,idMin,idMax,1);
		idSpinner = new JSpinner(idRange);
		idSpinner.setBounds(165, 32, 150, 25);
		frame.getContentPane().add(idSpinner);
		idSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				int recordCounts = currentUser.records.size();
				if(!(oldRecordId < 0 || currentRecordId < 0 ||
						oldRecordId >= recordCounts || currentRecordId >= recordCounts ||
						deleteInProgress)) {
					if(currentRecordId != oldRecordId) {
						currentRecordId = oldRecordId;
						if (currentUser.records.size()!=0){
							currentRecord = currentUser.records.get(currentRecordId);
							packedRecord = packRecord();
							compareDiscardOrApply(currentUser, currentRecordId, currentRecord, packedRecord);
							currentRecordId = (int) idSpinner.getValue();
							currentRecord = currentUser.records.get(currentRecordId);
							updateFields(currentRecord);
							//mainTable.clearSelection();
							mainTable.setRowSelectionInterval(currentRecordId, currentRecordId);
							System.out.println("DEBUG: INFO - old and new record IDs: " + oldRecordId + " -> " + currentRecordId);
							oldRecordId = currentRecordId;
							//updateFields(currentUser.records.get((int) currentUser.records.size() - 1));
						} else {
							System.out.println("DEBUG: INFO - Table is empty.");
						}
					} else {
						System.out.println("DEBUG: INFO - Discarded Reflexive call.");
					}
				} else {
					if(!deleteInProgress) {
						System.out.println("DEBUG: WARNING - currentRecord hits out of bound:" + currentRecordId);
					} else {
						System.out.println("DEBUG: INFO - supressing deletion while records update");
					}
				}
			}
		});
		
		retailerLocationText = new JTextField(10);
		retailerLocationText.setBounds(165, 215, 150, 25);
		frame.getContentPane().add(retailerLocationText);
		
		retailerNameText = new JTextField(10);
		retailerNameText.setBounds(165, 178, 150, 25);
		frame.getContentPane().add(retailerNameText);
		
		amountText = new JTextField(10);
		amountText.setBounds(165, 252, 150, 25);
		frame.getContentPane().add(amountText);
		
		otherDetailsText = new JTextField(10);
		otherDetailsText.setBounds(165, 326, 150, 25);
		frame.getContentPane().add(otherDetailsText);
		
		expenseTypeSelection = new JComboBox<expenseTypeE>();
		expenseTypeSelection.setModel(new DefaultComboBoxModel<expenseTypeE>(expenseTypeE.values()));
		expenseTypeSelection.setBounds(165, 142, 150, 25);
		frame.getContentPane().add(expenseTypeSelection);
		
		repetitionIntervalSelection = new JComboBox<repetitionIntervalE>();
		repetitionIntervalSelection.setModel(new DefaultComboBoxModel<repetitionIntervalE>(repetitionIntervalE.values()));
		repetitionIntervalSelection.setBounds(165, 364, 150, 25);
		frame.getContentPane().add(repetitionIntervalSelection);
		
		paymentTypeSelection = new JComboBox<ExpenseRecord.paymentTypeE>();
		paymentTypeSelection.setModel(new DefaultComboBoxModel<ExpenseRecord.paymentTypeE>(paymentTypeE.values()));
		paymentTypeSelection.setBounds(165, 105, 150, 25);
		frame.getContentPane().add(paymentTypeSelection);
		
		btnInsert = new JButton("Insert Record");
		btnInsert.setBounds(5, 410, 150, 25);
		frame.getContentPane().add(btnInsert);
		btnInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idRange.setMaximum(idMax++);
				System.out.println("DEBUG: INFO - IdSpinner Range [" + idRange.getMinimum().toString() + ":" + idRange.getMaximum().toString()+ "]");
				toggleButtons();
				currentRecord = packRecord();
				currentUser.records.add(currentRecord);
				model.addRow(currentRecord.getRecord());
				idSpinner.setValue(idRange.getMaximum());
			}
		});
		
		btnDelete = new JButton("Delete Record");
		btnDelete.setBounds(165, 410, 150, 25);
		frame.getContentPane().add(btnDelete);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteInProgress = true;
				if(idMax-- < 0)
					idMax = 0;
				idRange.setMaximum(idMax);
				System.out.println("DEBUG - INFO: IdSpinner Range [" + idRange.getMinimum().toString() + ":" + idRange.getMaximum().toString()+ "]");
				
				if(currentUser.records.size() > 0) {
					int[] ids = mainTable.getSelectedRows();
					if(mainTable.getSelectedColumnCount()!=0) {
						Object[] options = {"Confirm Deletion","Keep Records"};
						int confirmation = JOptionPane.showOptionDialog(frame, "Are you sure you want to delete the records [" + Integer.toString(ids[0]) + ":" + Integer.toString(ids[ids.length-1]) + "]", "Discard Changes?", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						if(confirmation == 0) {
							for (int id=0; id < ids.length ; id++) {
								System.out.println("Processing Id: " + ids[0]);
								model.removeRow(ids[0]);
						    	currentUser.records.remove(ids[0]);
							}
						}
						if(currentUser.records.size()!=0) {
							idSpinner.setValue(idRange.getMaximum());
							mainTable.clearSelection();
							mainTable.setRowSelectionInterval(0,0);
						} else {
							toggleButtons();
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Please select records to delete!");
					}
				}
				deleteInProgress = false;
			}
		});
		
		btnSave = new JButton("Apply changes");
		btnSave.setBounds(5, 437, 150, 25);
		frame.getContentPane().add(btnSave);
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				currentRecord = packRecord();
				applyChanges(currentUser,  currentRecord, currentRecordId);
			}
		});
		
		btnDiscard = new JButton("Discard changes");
		btnDiscard.setBounds(165, 437, 150, 25);
		frame.getContentPane().add(btnDiscard);
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				currentRecord = currentUser.records.get(currentRecordId);
				packedRecord = packRecord();
				compareDiscardOrApply(currentUser, currentRecordId, currentRecord, packedRecord);
			}
		});
		
		paidTick = new JCheckBox("Paid");
		paidTick.setBounds(25, 69, 50, 24);
		frame.getContentPane().add(paidTick);
		paidTick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paidDateSpinner.setVisible(paidTick.isSelected());
			}
		});
	}
	
	private void updateFields(ExpenseRecord record) {
		amountText.setText(Double.toString(record.getAmount()));
		paidTick.setSelected(record.getPaid());
		paymentTypeSelection.setSelectedIndex(record.getpaymentType().ordinal());
		expenseTypeSelection.setSelectedItem(record.getExpenseType().ordinal());
		repetitionIntervalSelection.setSelectedItem(record.getRepetitionInterval().ordinal());
		retailerNameText.setText(record.getRetailerName());
		retailerLocationText.setText(record.getRetailerLocation());
		operationDateSpinner.setValue(toDate(record.getOperationDate()));
		paidDateSpinner.setValue(toDate(record.getPaidDate()));
		otherDetailsText.setText(record.getOtherDetails());
	}
	
	// Creates a record from the form
	private ExpenseRecord packRecord() {
		/*{"Amount", "Paid", "Paid Date", "Expense Type", "Status Description",
		"Repetition Interval", "Retailer Name","Retailer Location","Operation Date",
		"Other Details"};*/
		Object[] formPackedRecord = {Double.parseDouble(amountText.getText()),
				paidTick.isSelected(),
				toLocalDate((Date) paidDateSpinner.getValue()),
				(expenseTypeE) expenseTypeSelection.getSelectedItem(),
				(paymentTypeE) paymentTypeSelection.getSelectedItem(),
				(repetitionIntervalE) repetitionIntervalSelection.getSelectedItem(),
				retailerNameText.getText(),
				retailerLocationText.getText(),
				toLocalDate((Date) operationDateSpinner.getValue()),
				otherDetailsText.getText()
		};
		return new ExpenseRecord(formPackedRecord);
	}
	
	private void applyChanges(User currentUser, ExpenseRecord targetRecord, int targetId) {
		currentUser.records.remove(targetId);
		model.removeRow(targetId);
		currentUser.records.add(targetId, targetRecord);
		model.insertRow(targetId, targetRecord.getRecord());
		mainTable.clearSelection();
		mainTable.setRowSelectionInterval(targetId, targetId);
	}
	
	private void compareDiscardOrApply(User currentUser, int currentRecordId, ExpenseRecord currentRecord, ExpenseRecord packedRecord) {
		boolean comparisonResult = !Arrays.equals(currentRecord.getRecord(), packedRecord.getRecord());
		if (comparisonResult){
		    Object[] options = {"Discard Changes","Apply Changes"};
		    if(JOptionPane.showOptionDialog(frame, "Apply changes?", "Confirmation", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]) == 1)
		    	applyChanges(currentUser,packedRecord,currentRecordId);
		    else
		    	updateFields(currentRecord);
		}
	}
	
    private void setUpColumnInputModels() {
// /*<== multi-line tag starting from here for proper access to Design view, just delete '//'
    	TableColumn paidCol = mainTable.getColumnModel().getColumn(ExpenseRecord.recordFieldE.paid.ordinal());
    	TableColumn expenseTypeCol = mainTable.getColumnModel().getColumn(ExpenseRecord.recordFieldE.expenseType.ordinal());
    	TableColumn repetitionIntervalCol = mainTable.getColumnModel().getColumn(ExpenseRecord.recordFieldE.repetitionInterval.ordinal());
    	TableColumn paymentTypeCol = mainTable.getColumnModel().getColumn(ExpenseRecord.recordFieldE.paymentType.ordinal());

    	//Set up the editors for the designated columns.
    	JCheckBox paidCheck = new JCheckBox();
    	JComboBox<expenseTypeE> expenseTypeCombo = new JComboBox<expenseTypeE>(new expenseTypeE[] {expenseTypeE.Purchase,expenseTypeE.Bill});
    	JComboBox<repetitionIntervalE> repetitionIntervalCombo = new JComboBox<repetitionIntervalE>(new repetitionIntervalE[] {repetitionIntervalE.Once,repetitionIntervalE.Day,repetitionIntervalE.Bidaily,repetitionIntervalE.Weekly,repetitionIntervalE.Biweekly,repetitionIntervalE.Monthly,repetitionIntervalE.Yearly});
    	JComboBox<paymentTypeE>paymentTypeCombo = new JComboBox<paymentTypeE>(new paymentTypeE[] {paymentTypeE.paidByCash,paymentTypeE.paidByDebit, paymentTypeE.dueByCredit});
    	
    	paidCol.setCellEditor(new DefaultCellEditor(paidCheck));
    	expenseTypeCol.setCellEditor(new DefaultCellEditor(expenseTypeCombo));
    	repetitionIntervalCol.setCellEditor(new DefaultCellEditor(repetitionIntervalCombo));
    	paymentTypeCol.setCellEditor(new DefaultCellEditor(paymentTypeCombo));
    	
    	paidCol.setCellRenderer(new DefaultTableCellRenderer());
    	expenseTypeCol.setCellRenderer(new DefaultTableCellRenderer());
    	repetitionIntervalCol.setCellRenderer(new DefaultTableCellRenderer());
    	paymentTypeCol.setCellRenderer(new DefaultTableCellRenderer());
//<== multi-line tag ending to here for proper Design view */
    }
    private LocalDate toLocalDate(Date input) {return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();}
    private Date toDate(LocalDate input) {return Date.from(input.atStartOfDay(ZoneId.systemDefault()).toInstant());}
    private void toggleButtons(){
    	toggleCounter++;
    	if (toggleCounter % 2 == 0) {
			btnSave.setEnabled(false);
			btnDelete.setEnabled(false);
			btnDiscard.setEnabled(false);
    	} else {
			btnSave.setEnabled(true);
			btnDelete.setEnabled(true);
			btnDiscard.setEnabled(true);
    	}
    }
}