package com.concordia.personalBudgetManager;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.recordFieldE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

public class RecordManagementForm {
	
	private ExpenseRecord currentRecord, packedRecord, newRecord;
	private int currentRecordId, oldRecordId=0;
	private boolean deleteInProgress = false; 

	private JFrame frame;
	private User currentUser;
	private JTable mainTable, subTable;
	private DefaultTableModel mainModel, subModel;

	private JSpinner idSpinner, paidDateSpinner, operationDateSpinner;
	private SpinnerNumberModel idRange;
	private int idMax = 0, idMin = 0;
	
	private JCheckBox paidTick, showMainPaid, showSubPaid;
	private JComboBox<expenseTypeE> expenseTypeSelection;
	private JComboBox<repetitionIntervalE> repetitionIntervalSelection;
	private JComboBox<paymentTypeE> paymentTypeSelection;
	private JTextField retailerNameText, retailerLocationText, amountText, otherDetailsText;
	
	private Container p;
	private JScrollPane mainPane, subPane;
	private JLabel l1, l2, l3, l4, l5, l6, l7, l8, l9;
	private JButton mainInsert, mainDelete, mainSave, mainDiscard;
	private JButton subInsert, subSave, subDelete, subDiscard;
	private JButton btnPullFromDb, btnPushToDb;
	
	// Create the application.
	public RecordManagementForm(User parsedUser) {
		currentUser = parsedUser;
		parsedUser = null;
		this.setLookAndFeel("Nimbus");
		initialize();
		enumerateMain();
		//frame.getContentPane().setLayout(null);
		frame.setVisible(true);
		amountText.setText("0.0");
		
		if(currentUser.records.size()!=0) {
			mainTable.setRowSelectionInterval(0, 0);			
		} else {			
			disableMain();
		}
	}
	
	// Set the "look and feel" of the form
	private void setLookAndFeel(String themeName) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if (themeName.equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
				UIManager.setLookAndFeel(info.getClassName()); // Set theme to last available theme
			}
		} catch (Exception e) {
			System.out.println("DEBUG: ERROR - [" + themeName + "] theme is not available");
			// If the given theme is not available, you can set the GUI to another look and feel.
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("[" + currentUser.name + "] expense records");
		frame.setBounds(100, 100, 1300, 520);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		p = frame.getContentPane();
		p.setLayout(null);
		
		btnPushToDb = new JButton("^ Push to DB ^");
		btnPushToDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				syncPush();		// PUSH TO DB
			}
		});
		btnPushToDb.setBounds(500, 5, 150, 25);
		frame.getContentPane().add(btnPushToDb);

		btnPullFromDb = new JButton("v Pull from DB v");
		btnPullFromDb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				syncPull();		// PULL FROM DB
			}
		});
		btnPullFromDb.setBounds(661, 5, 150, 25);
		frame.getContentPane().add(btnPullFromDb);
		
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
		paidDateSpinner.setEnabled(false);
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
							currentRecordId = (int) idSpinner.getValue();
							currentRecord = currentUser.records.get(currentRecordId);
							updateFields(currentRecord, false);
							mainTable.setRowSelectionInterval(currentRecordId, currentRecordId);
							System.out.println("DEBUG: INFO - old and new record IDs: " + oldRecordId + " -> " + currentRecordId);
							oldRecordId = currentRecordId;
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
		
		mainInsert = new JButton("Insert Record");
		mainInsert.setBounds(5, 410, 150, 25);
		frame.getContentPane().add(mainInsert);
		mainInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				idRange.setMaximum(idMax++);
				System.out.println("DEBUG: INFO - IdSpinner Range [" + idRange.getMinimum().toString() + ":" + idRange.getMaximum().toString()+ "]");
				enableMain();
				currentRecord = packRecord(true); // discard sub records
				currentUser.records.add(currentRecord);
				mainModel.addRow(currentRecord.getRecord());
				//idSpinner.setValue(idRange.getMaximum());
			}
		});
		
		mainDelete = new JButton("Delete Record");
		mainDelete.setBounds(165, 410, 150, 25);
		frame.getContentPane().add(mainDelete);
		mainDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteInProgress = true;
				if(idMax-- < 0)
					idMax = 0;
				idRange.setMaximum(idMax);
				System.out.println("DEBUG: INFO - IdSpinner Range [" + idRange.getMinimum().toString() + ":" + idRange.getMaximum().toString()+ "]");
				
				if(currentUser.records.size() > 0) {
					int mainAt = mainTable.getSelectedRow();
					if(mainTable.getSelectedColumnCount()!=0) {
						Object[] options = {"Confirm Deletion","Keep Records"};
						int confirmation = JOptionPane.showOptionDialog(frame, "Are you sure you want to delete record [" + Integer.toString(mainAt) + "]", "Discard Changes?", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
						if(confirmation == 0) {
							System.out.println("Processing Id: " + mainAt);
							mainModel.removeRow(mainAt);
							currentUser.records.remove(mainAt);
						}
						if(currentUser.records.size()!=0) {
							oldRecordId--; // Important for change tracking
							idSpinner.setValue(idRange.getMaximum());
							mainAt = mainAt==0 ? 1 : mainAt;
							mainTable.setRowSelectionInterval(mainAt-1,mainAt-1);
							updateFields(currentUser.records.get(mainAt-1), false);
						} else {
							disableMain();
						}
					} else {
						JOptionPane.showMessageDialog(frame, "Please select records to delete!");
					}
				}
				deleteInProgress = false;
			}
		});
		
		mainSave = new JButton("Apply Changes");
		mainSave.setBounds(5, 437, 150, 25);
		frame.getContentPane().add(mainSave);
		mainSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				currentRecord = packRecord(false);

				// Apply changes //
				currentUser.records.set(currentRecordId, currentRecord);
				mainModel.removeRow(currentRecordId);
				mainModel.insertRow(currentRecordId, currentRecord.getRecord());
				mainTable.setRowSelectionInterval(currentRecordId, currentRecordId);
				updateFields(currentRecord, false);
			}
		});
		
		mainDiscard = new JButton("Cancel changes");
		mainDiscard.setBounds(165, 437, 150, 25);
		frame.getContentPane().add(mainDiscard);
		mainDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				currentRecord = currentUser.records.get(currentRecordId);
				updateFields(currentRecord, false);
			}
		});
		
		paidTick = new JCheckBox("Paid");
		paidTick.setBounds(25, 69, 50, 24);
		frame.getContentPane().add(paidTick);
		paidTick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paidDateSpinner.setEnabled(paidTick.isSelected());
			}
		});
		
		subInsert = new JButton("Insert Sub Record");
		subInsert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				currentRecordId = (int) idSpinner.getValue();
				currentRecord = currentUser.records.get(currentRecordId);
				packedRecord = packRecord(true);
				if(!packedRecord.getExpenseType().equals(ExpenseRecord.expenseTypeE.Composite)) {
					currentRecord.addSubRecord(packedRecord);
				} else {
					JOptionPane.showMessageDialog(frame, "Change record type. Composite is not allowed here", "Composite not allowed here.", JOptionPane.ERROR_MESSAGE);
				}
				enumerateSub(currentRecord);
				updateMainModel(currentRecordId);
			}
		});
		subInsert.setBounds(343, 360, 150, 25);
		frame.getContentPane().add(subInsert);
		
		subSave = new JButton("Apply Sub Changes");
		subSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int subAt = subTable.getSelectedRow();
				int mainAt = (int) idSpinner.getValue();
				if(subAt != -1) {
					packedRecord = packRecord(true);
					currentUser.records.get(mainAt).removeSubRecord(subAt);
					currentUser.records.get(mainAt).insertSubRecord(subAt,packedRecord);
					subModel.removeRow(subAt);
					subModel.insertRow(subAt,packedRecord.getRecord());
					if(currentUser.records.get(mainAt).getSubRecordsCount() == 0) {
						disableSub();
					} else {
						// Highlights next row and update fields accordingly
						subAt = subAt == 0 ? 1 : subAt;
						subTable.setRowSelectionInterval(subAt, subAt);
						updateFields(currentUser.records.get(mainAt).getSubRecord(subAt), true);
					}
				}
				updateMainModel(mainAt);
			}
		});
		subSave.setEnabled(false);
		subSave.setBounds(343, 385, 150, 25);
		frame.getContentPane().add(subSave);
		
		subDelete = new JButton("Delete Sub Record");
		subDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int subAt = subTable.getSelectedRow();
				int mainAt = (int) idSpinner.getValue();
				if(subAt != -1) {
					currentUser.records.get(mainAt).removeSubRecord(subAt);
					subModel.removeRow(subAt);
					if(currentUser.records.get(mainAt).getSubRecordsCount() == 0) {
						disableSub();
					} else {
						// Highlights next row and update fields accordingly
						subAt = subAt == 0 ? 1 : subAt;
						subTable.setRowSelectionInterval(subAt-1, subAt-1);
						updateFields(currentUser.records.get(mainAt).getSubRecord(subAt-1), true);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Please select sub records to delete!");
				}
				updateMainModel(mainAt);
			}
		});
		subDelete.setEnabled(false);
		subDelete.setBounds(343, 410, 150, 25);
		frame.getContentPane().add(subDelete);
		
		subDiscard = new JButton("Cancel Sub Changes");
		subDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int subAt = subTable.getSelectedRow();
				int mainAt = (int) idSpinner.getValue();
				if(subAt != -1) {
					currentRecord = currentUser.records.get(mainAt).getSubRecord(subAt);
					updateFields(currentRecord, true);
				}
				updateMainModel(mainAt);
			}
		});
		subDiscard.setEnabled(false);
		subDiscard.setBounds(343, 435, 150, 25);
		frame.getContentPane().add(subDiscard);
				
		mainModel = new DefaultTableModel(ExpenseRecord.recordFieldStrings,0) {
			// Makes the table read-only
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {return false;}
		};
		
		mainTable = new JTable(mainModel);
		mainTable.setBounds(384, 32, 1186, 509);
		mainTable.setRowSelectionAllowed(true);
		mainTable.setAutoCreateRowSorter(true); //Vlad - this line enables sorting by clicking headers
		mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int mainAt = mainTable.getSelectedRow();
				if(mainAt != -1) {
					idSpinner.setValue(mainAt);
					currentRecord = currentUser.records.get(mainAt);
					enableMain();						//Vlad - if a row is selected, we need to activate buttons the buttons.
					updateFields(currentRecord, false);	//Vlad - if a row is selected, [BQ] update form fields.
				}
			}
		});

		mainPane = new JScrollPane();
		hideSub();		//mainPane.setBounds(343, 32, 933, 430);
		mainPane.setViewportView(mainTable);
		frame.getContentPane().add(mainPane);
		
		subModel = new DefaultTableModel(ExpenseRecord.recordFieldStrings,0) {
			// Makes the table read-only
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {return false;}
		};
		
		subTable = new JTable(subModel);
		subTable.setRowSelectionAllowed(true);
		subTable.setAutoCreateRowSorter(true);
		subTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		subTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int subAt = subTable.getSelectedRow();
				if(subAt != -1) {
					int mainAt = (int) idSpinner.getValue();
					currentRecord = currentUser.records.get(mainAt).getSubRecord(subAt);
					enableSub();						//Vlad - if a row is selected, we need to activate buttons the buttons.
					updateFields(currentRecord, true);	//Vlad - if a row is selected, [BQ] update form fields.
				}
			}
		});
		
		subPane = new JScrollPane();
		subPane.setBounds(505, 327, 771, 133);
		subPane.setViewportView(subTable);
		frame.getContentPane().add(subPane);
		
		//Vlad - checkbox to show and hide paid expenses
		showMainPaid = new JCheckBox("Hide paid");
		showMainPaid.setSelected(false);
		showMainPaid.setBounds(343, 7, 150, 23);
		showMainPaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableRowSorter<TableModel> sorter = new TableRowSorter<>(mainTable.getModel());
				if (showMainPaid.isSelected()) {
					sorter.setRowFilter(RowFilter.regexFilter("false"));
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("[truefalse]"));
				}
				mainTable.setRowSorter(sorter);
			}
		});
		frame.getContentPane().add(showMainPaid);
		
		showSubPaid = new JCheckBox("Hide sub paid");
		showSubPaid.setSelected(false);
		showSubPaid.setBounds(343, 335, 150, 23);
		showSubPaid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableRowSorter<TableModel> sorter = new TableRowSorter<>(subTable.getModel());
				if (showSubPaid.isSelected()) {
					sorter.setRowFilter(RowFilter.regexFilter("false"));
				} else {
					sorter.setRowFilter(RowFilter.regexFilter("[truefalse]"));
				}
				subTable.setRowSorter(sorter);
			}
		});
		frame.getContentPane().add(showSubPaid);
		
	}
	
	private void updateFields(ExpenseRecord record, boolean isSubRecord) {
		amountText.setText(Double.toString(record.getAmount()));
		paidTick.setSelected(record.getPaid());
		paymentTypeSelection.setSelectedIndex(record.getPaymentType().ordinal());
		expenseTypeSelection.setSelectedIndex(record.getExpenseType().ordinal());
		repetitionIntervalSelection.setSelectedIndex(record.getRepetitionInterval().ordinal());
		retailerNameText.setText(record.getRetailerName());
		retailerLocationText.setText(record.getRetailerLocation());
		operationDateSpinner.setValue(toDate(record.getOperationDate()));
		paidDateSpinner.setValue(toDate(record.getPaidDate()));
		otherDetailsText.setText(record.getOtherDetails());
		if(!isSubRecord)
			enumerateSub(record);
	}
	
	public ExpenseRecord packRecord(boolean isSubRecord) {
		// Creates a record from the form
		Object[] formPackedRecord = {
				Double.parseDouble(amountText.getText()),
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
		currentRecordId = (int) idSpinner.getValue();
		newRecord = new ExpenseRecord(formPackedRecord);
		if(!isSubRecord) {
			//Rebuilds the subRecords for the new object
			int subRecordsCount = currentUser.records.get(currentRecordId).getSubRecordsCount();
			for(int subRecordId = 0; subRecordId < subRecordsCount; subRecordId++) {
				ExpenseRecord subRecord = currentUser.records.get(currentRecordId).getSubRecord(0);
				subRecord.setPaid((boolean)formPackedRecord[recordFieldE.paid.ordinal()]);
				subRecord.setPaidDate((LocalDate)formPackedRecord[recordFieldE.paidDate.ordinal()]);
				newRecord.addSubRecord(subRecord);
				currentUser.records.get(currentRecordId).removeSubRecord(0);
			}
		}
		return newRecord;
	}
				
	public LocalDate toLocalDate(Date input) {return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();}
	private Date toDate(LocalDate input) {return Date.from(input.atStartOfDay(ZoneId.systemDefault()).toInstant());}

	private void enableMain() {
		mainSave.setEnabled(true);
		mainDelete.setEnabled(true);
		mainDiscard.setEnabled(true);
	}
	
	private void disableMain() {
		mainSave.setEnabled(false);
		mainDelete.setEnabled(false);
		mainDiscard.setEnabled(false);		
	}
	
	private void enableSub() {
		subSave.setEnabled(true);
		subDelete.setEnabled(true);
		subDiscard.setEnabled(true);
	}

	private void disableSub() {
		subSave.setEnabled(false);
		subDelete.setEnabled(false);
		subDiscard.setEnabled(false);		
	}
	
	private void showSub() {
		mainPane.setBounds(343, 32, 933, 283);
		subInsert.setVisible(true);
		subSave.setVisible(true);
		subDelete.setVisible(true);
		subDiscard.setVisible(true);
		enableSub();
	}
	
	private void hideSub() {
		mainPane.setBounds(343, 32, 933, 430);
		subInsert.setVisible(false);
		subSave.setVisible(false);
		subDelete.setVisible(false);
		subDiscard.setVisible(false);
		disableSub();
	}
	
	private void enumerateMain() {
		idRange.setMinimum(0); idRange.setMaximum(currentUser.records.size()-1);
		for(int i=0; i<currentUser.records.size(); i++)
			mainModel.addRow(currentUser.records.get(i).getRecord());
	}
	
	private void enumerateSub(ExpenseRecord currentRecord) {
		int subRecordsCount = currentRecord.getSubRecordsCount();
		if(currentRecord.getExpenseType().equals(expenseTypeE.Composite)) {
			showSub();
			if(subRecordsCount==0)
				disableSub();
			else
				enableSub();
			int modelRows = subModel.getRowCount(); for(int i=0; i<modelRows; i++) {subModel.removeRow(0);} // Removes old values
			for(int i=0; i<subRecordsCount; i++) {subModel.addRow(currentRecord.getSubRecord(i).getRecord());} // Inserts new ones and updates master field
		} else {
			hideSub();
		}
	}
	
	private void updateMainModel(int mainAt) {
		mainModel.setValueAt(currentUser.records.get(mainAt).getAmount(), mainAt, recordFieldE.amount.ordinal());
		mainModel.setValueAt(currentUser.records.get(mainAt).getPaid(), mainAt, recordFieldE.paid.ordinal());
		//amountText.setText(Double.toString(currentUser.records.get(mainAt).getAmount()));
	}
	
	private void syncPull() {
		Object[] options = {"Confirm Sync","Return to current records"};
		int confirmation = JOptionPane.showOptionDialog(frame, "Are you sure you want to revert to database records", "New Start?", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if(confirmation == 0) {
			try {
				final DatabaseManager db = new DatabaseManager();
				Connection con = db.sqlConnect();
				//String mainQuery = "SELECT [recordId],[amount],[paid],[paidDate],[expenseType],[paymentType],[repetitionInterval],[retailerName],[retailerLocation],[operationDate],[otherDetails] FROM [dbo].[expensesRecords]";
				String mainQuery = "SELECT * FROM [dbo].[mainRecords];";
				ResultSet mRs = db.sqlRequest(con, mainQuery);
				// Remove the old records
				int recordsSize = currentUser.records.size();
				for(int i=0; i<recordsSize; i++)
					currentUser.records.remove(0);
				// Add the fresh records from the DB
				while (mRs.next()) {
					Object mArr[] = getDbRecords(mRs);
					currentUser.records.add(new ExpenseRecord(mArr));
					int recId = mRs.getInt(mRs.findColumn("recordId"));
					System.out.println("DEBUG: INFO - Reading: " + Arrays.deepToString(mArr));
					if(expenseTypeE.Composite.equals(mArr[ExpenseRecord.recordFieldE.expenseType.ordinal()])) { //Checks if 'composite'
						String subQuery = "SELECT * FROM [dbo].[subRecords] WHERE [recordId] = " + Integer.toString(recId) + ";";
						ResultSet sRs = db.sqlRequest(con, subQuery);
						while (sRs.next()) {
							Object sArr[] = getDbRecords(sRs);
							currentUser.records.get(currentUser.records.size()-1).addSubRecord(new ExpenseRecord(sArr));
						}
					}
				}
				con.close();
				
				// Flush old records in the table
				int modelRecordsCount = mainModel.getRowCount();
				for(int i=0; i<modelRecordsCount; i++)
					mainModel.removeRow(0);
	
				// Insert fresh records
				enumerateMain();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Object[] getDbRecords(ResultSet rs) throws SQLException{
		int cols = rs.getMetaData().getColumnCount();
		Object[] arr = new Object[cols];
		arr[ExpenseRecord.recordFieldE.amount.ordinal()]			= rs.getDouble(rs.findColumn(ExpenseRecord.recordFieldE.amount.toString()));
		arr[ExpenseRecord.recordFieldE.paid.ordinal()]				= rs.getBoolean(rs.findColumn(ExpenseRecord.recordFieldE.paid.toString()));
		arr[ExpenseRecord.recordFieldE.paidDate.ordinal()]			= (rs.getDate(rs.findColumn(ExpenseRecord.recordFieldE.paidDate.toString()))).toLocalDate();
		arr[ExpenseRecord.recordFieldE.expenseType.ordinal()]		= expenseTypeE.values()[rs.getInt(rs.findColumn(ExpenseRecord.recordFieldE.expenseType.toString()))];
		arr[ExpenseRecord.recordFieldE.paymentType.ordinal()]		= paymentTypeE.values()[rs.getInt(rs.findColumn(ExpenseRecord.recordFieldE.paymentType.toString()))];
		arr[ExpenseRecord.recordFieldE.repetitionInterval.ordinal()]= repetitionIntervalE.values()[rs.getInt(rs.findColumn(ExpenseRecord.recordFieldE.repetitionInterval.toString()))];
		arr[ExpenseRecord.recordFieldE.retailerName.ordinal()]		= rs.getString(rs.findColumn(ExpenseRecord.recordFieldE.retailerName.toString()));
		arr[ExpenseRecord.recordFieldE.retailerLocation.ordinal()]	= rs.getString(rs.findColumn(ExpenseRecord.recordFieldE.retailerLocation.toString()));
		arr[ExpenseRecord.recordFieldE.operationDate.ordinal()]		= (rs.getDate(rs.findColumn(ExpenseRecord.recordFieldE.operationDate.toString()))).toLocalDate();
		arr[ExpenseRecord.recordFieldE.otherDetails.ordinal()]		= rs.getString(rs.findColumn(ExpenseRecord.recordFieldE.otherDetails.toString()));
		return arr;
	}
	
	private void syncPush() {
		Object[] options = {"Confirm Sync","Return to current records"};
		int confirmation = JOptionPane.showOptionDialog(frame, "Are you sure you want to overwrite database records", "Save changes to DB?", JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		if(confirmation == 0) {
			try {
				final DatabaseManager db = new DatabaseManager();
				Connection con = db.sqlConnect();
				//String mainQuery = "SELECT [recordId],[amount],[paid],[paidDate],[expenseType],[paymentType],[repetitionInterval],[retailerName],[retailerLocation],[operationDate],[otherDetails] FROM [dbo].[expensesRecords]";
				// Remove all main and sub records from database
				db.sqlOrder(con, "DELETE FROM [dbo].[mainRecords];");
				db.sqlOrder(con, "DELETE FROM [dbo].[subRecords];");
				for (int recordId=0; recordId<currentUser.records.size(); recordId++) {
					String mainQuery = mainWrite(recordId+1, currentUser.records.get(recordId));
					db.sqlOrder(con, mainQuery); // Do the writing query
					if(expenseTypeE.Composite.equals(currentUser.records.get(recordId).getExpenseType())) { //Checks if 'composite'
						for(int subRecId=0; subRecId<currentUser.records.get(recordId).getSubRecordsCount(); subRecId++) {
							String subQuery = subWrite(recordId+1, subRecId+1, currentUser.records.get(recordId).getSubRecord(subRecId));
							db.sqlOrder(con, subQuery);
						}
					}
				}
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String mainWrite(int recordId, ExpenseRecord recordNow) {
		return "INSERT INTO [dbo].[mainRecords] ([recordId],[amount],[paid],[paidDate],[expenseType],[paymentType],[repetitionInterval],[retailerName],[retailerLocation],[operationDate],[otherDetails]) VALUES ('"
				+ Integer.toString(recordId) + "','"
				+ Double.toString(recordNow.getAmount()) + "','"
				+ Boolean.toString(recordNow.getPaid()) + "','"
				+ (recordNow.getPaidDate()).toString() + "','"
				+ Integer.toString(recordNow.getExpenseType().ordinal()) + "','"
				+ Integer.toString(recordNow.getPaymentType().ordinal()) + "','"
				+ Integer.toString(recordNow.getRepetitionInterval().ordinal()) + "','"
				+ recordNow.getRetailerName() + "','"
				+ recordNow.getRetailerLocation() + "','"
				+ (recordNow.getOperationDate()).toString() + "','"
				+ recordNow.getOtherDetails() + "')";
	}
	
	private String subWrite(double subId, int mainId, ExpenseRecord recordNow) {
		return "INSERT INTO [dbo].[subRecords] ([recordId],[subRecordId],[amount],[paid],[paidDate],[expenseType],[paymentType],[repetitionInterval],[retailerName],[retailerLocation],[operationDate],[otherDetails]) VALUES ('"
				+ Integer.toString(mainId) + "','"
				+ Double.toString(mainId + subId/100) + "','"
				+ Double.toString(recordNow.getAmount()) + "','"
				+ Boolean.toString(recordNow.getPaid()) + "','"
				+ (recordNow.getPaidDate()).toString() + "','"
				+ Integer.toString(recordNow.getExpenseType().ordinal()) + "','"
				+ Integer.toString(recordNow.getPaymentType().ordinal()) + "','"
				+ Integer.toString(recordNow.getRepetitionInterval().ordinal()) + "','"
				+ recordNow.getRetailerName() + "','"
				+ recordNow.getRetailerLocation() + "','"
				+ (recordNow.getOperationDate()).toString() + "','"
				+ recordNow.getOtherDetails() + "')";
	}
}