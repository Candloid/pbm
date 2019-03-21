package test;

import com.concordia.personalBudgetManager.ExpenseRecord;
import com.concordia.personalBudgetManager.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class UserTest {

    User currentUser;

    @Before
    public void setUp() throws Exception {
        this.currentUser = new User("test_user");
    }

    @Test
    public void testPackRecord() {
        //when
        ArrayList<ExpenseRecord> expenseRecords = new ArrayList<>();
        expenseRecords.add(new ExpenseRecord());
        //this.currentUser.setRecords(expenseRecords);
        //then
        assertEquals("test_user", currentUser.getName());
        //assertEquals(1, currentUser.getRecords().size());
    }


}