package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import com.concordia.personalBudgetManager.ExpenseRecord;
import com.concordia.personalBudgetManager.RecordManagementForm;
import com.concordia.personalBudgetManager.User;
import org.junit.jupiter.api.Test;

import com.concordia.personalBudgetManager.ExpenseRecord.expenseTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.paymentTypeE;
import com.concordia.personalBudgetManager.ExpenseRecord.repetitionIntervalE;

class ExpenseRecordTest {
    // Initialize a random user, two matching records from two different constructors.
    private static String Username = "Random User";
    private static User randomUser = new User(Username);
    public static RecordManagementForm userForm = new RecordManagementForm(randomUser);
    ExpenseRecord myRecord = new ExpenseRecord(ExpenseRecord.nullRecord);

    public static double specificAmount = 88.88;
    public static boolean specificPaid = true;
    public static LocalDate specificPaymentDate = LocalDate.now();
    public static expenseTypeE specificExpenseTypeE = expenseTypeE.Bill;
    public static paymentTypeE specificPaymentTypeE = paymentTypeE.paidByDebit;
    public static repetitionIntervalE specificRepetitionIntervalE = repetitionIntervalE.Yearly;
    public static String specificRetailerName = "Retailer Name";
    public static String specificRetailerLocation = "Retailer Location";
    public static LocalDate specificOperationDate = LocalDate.now();
    public static String specificDetails = "Details";

    public static Object[] specificObject = {88.88,
            true,
            LocalDate.now(),
            expenseTypeE.Bill,
            paymentTypeE.paidByDebit,
            repetitionIntervalE.Yearly,
            "Retailer Name",
            "Retailer Location",
            LocalDate.now(),
            "Details"};

    public ExpenseRecord specificRecordByObject = new ExpenseRecord(specificObject);
    public ExpenseRecord specificRecordByParameters = new ExpenseRecord(specificAmount, specificPaid, specificPaymentDate, specificExpenseTypeE, specificPaymentTypeE, specificRepetitionIntervalE, specificRetailerName, specificRetailerLocation, specificOperationDate, specificDetails);
    public ExpenseRecord nullRecord = new ExpenseRecord();

    // Tests the integrity of the nullRecord
    @Test
    void testNullRecord() {
        assertEquals(nullRecord.getRecord(), nullRecord.nullRecord);
    }

    // Tests if the constructor parses the same values for the Object[] or the parameters one-by-one in both cases
    @Test
    void testConstructor() {
        assertEquals(specificRecordByObject.getRecord(), specificRecordByParameters.getRecord());
    }

    // Test if the constructor properly parsed all of the parameters as intended for object creation method
    @Test
    void testRecordbyObjectGets() {
        assertEquals(specificRecordByObject.getAmount(), specificAmount, 0.01);
        assertEquals(specificRecordByObject.getPaid(), specificPaid);
        assertEquals(specificRecordByObject.getPaidDate(), specificPaymentDate);
        assertEquals(specificRecordByObject.getExpenseType(), specificExpenseTypeE);
        assertEquals(specificRecordByObject.getpaymentType(), specificPaymentTypeE);
        assertEquals(specificRecordByObject.getRepetitionInterval(), specificRepetitionIntervalE);
        assertEquals(specificRecordByObject.getRetailerLocation(), specificRetailerLocation);
        assertEquals(specificRecordByObject.getRetailerName(), specificRetailerName);
        assertEquals(specificRecordByObject.getOtherDetails(), specificDetails);
        assertEquals(specificRecordByObject.getRecord(), specificObject);
    }

    // Test if the constructor properly parsed all of the parameters as intended for parameters creation method
    @Test
    void testRecordByParametersGets() {
        assertEquals(specificRecordByParameters.getAmount(), specificAmount, 0.01);
        assertEquals(specificRecordByParameters.getPaid(), specificPaid);
        assertEquals(specificRecordByParameters.getPaidDate(), specificPaymentDate);
        assertEquals(specificRecordByParameters.getExpenseType(), specificExpenseTypeE);
        assertEquals(specificRecordByParameters.getpaymentType(), specificPaymentTypeE);
        assertEquals(specificRecordByParameters.getRepetitionInterval(), specificRepetitionIntervalE);
        assertEquals(specificRecordByParameters.getRetailerLocation(), specificRetailerLocation);
        assertEquals(specificRecordByParameters.getRetailerName(), specificRetailerName);
        assertEquals(specificRecordByParameters.getOtherDetails(), specificDetails);
        assertEquals(specificRecordByParameters.getRecord(), specificObject);
    }

    @Test
    void testGetAmount() {
        //GIVEN
        ExpenseRecord expenseRecord = new ExpenseRecord();
        expenseRecord.setAmount(23d);
        //WHEN
        double actualAmount = expenseRecord.getAmount();
        //THEN
        assertTrue(23d == actualAmount);

    }


    @Test
    void testGetPaid() {
        //GIVEN
        ExpenseRecord expenseRecord = new ExpenseRecord();
        expenseRecord.setPaid(true);
        //WHEN
        boolean actualIsPaid = expenseRecord.getPaid();
        //THEN
        assertEquals(true, actualIsPaid);

    }

    @Test
    void testGetPaidDate() {

        //GIVEN
        ExpenseRecord expenseRecord = new ExpenseRecord();
        LocalDate localDate = LocalDate.now();
        expenseRecord.setPaidDate(localDate);
        //WHEN
        LocalDate actualExpectedDate = expenseRecord.getPaidDate();
        //THEN
        assertEquals(localDate, actualExpectedDate);

    }

    @Test
    void testGetExpenseType() {

        //GIVEN
        ExpenseRecord expenseRecord = new ExpenseRecord();
        expenseRecord.setExpenseType(expenseTypeE.Bill);
        //WHEN
        expenseTypeE actualExpenseType = expenseRecord.getExpenseType();
        //THEN
        assertEquals(expenseTypeE.Bill, actualExpenseType);

    }

}
