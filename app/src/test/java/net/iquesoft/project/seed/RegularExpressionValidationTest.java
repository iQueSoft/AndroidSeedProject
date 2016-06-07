package net.iquesoft.project.seed;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.utils.Constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegularExpressionValidationTest {
    RegularExpressionValidation validation = new RegularExpressionValidation();


    @Test
    public void testIsEmailValid() {
        int expectedValue = Constants.CODE_OK;
        String s = "testing1.m_m2@email3.com4.ua5.net";
        int actualValue = validation.isEmailValid(s);
        assertEquals("Email " + s + " validation failed", expectedValue, actualValue);

    }

    @Test
    public void testIsNameValid() {
        int expectedValue = Constants.CODE_OK;
        String s = "Testing_Name123-abc";
        int actualValue = validation.isNameValid(s);
        assertEquals("Name " + s + " validation failed", expectedValue, actualValue);
    }

    @Test
    public void testIsPasswordValid() {
        int expectedValue = Constants.CODE_OK;
        String s = "Password123";
        int actualValue = validation.isPasswordValid(s);
        assertEquals("Password " + s + " validation failed", expectedValue, actualValue);
    }
}
