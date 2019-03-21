package test;

import com.concordia.personalBudgetManager.ExpenseRecord;
import com.concordia.personalBudgetManager.RecordManagementForm;
import com.concordia.personalBudgetManager.User;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class RecordManagementFormTest {

    RecordManagementForm recordManagementForm;

    @Before
    public void setUp() throws Exception {
        User currentUser = new User("test_user");
        recordManagementForm = new RecordManagementForm(currentUser);

    }

    @Test
    //? Ryan ?
    public void testPackRecord() {
        //when
        ExpenseRecord expenseRecord = recordManagementForm.packRecord(true);
        //then
        assertEquals(ExpenseRecord.expenseTypeE.Purchase, expenseRecord.getExpenseType());
        assertEquals(ExpenseRecord.paymentTypeE.paidByCash, expenseRecord.getPaymentType());
        assertEquals(ExpenseRecord.repetitionIntervalE.Once, expenseRecord.getRepetitionInterval());

    }

    @Test
    public void testToLocalDate() {
        //given
        Date date = new Date();
        LocalDate expectedLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //when
        LocalDate actualLocalDate = recordManagementForm.toLocalDate(date);
        //then
        assertEquals(expectedLocalDate, actualLocalDate);
    }
}