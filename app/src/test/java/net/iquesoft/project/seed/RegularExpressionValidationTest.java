package net.iquesoft.project.seed;

import net.iquesoft.project.seed.domain.RegularExpressionValidation;
import net.iquesoft.project.seed.domain.exception.EmptyFieldException;
import net.iquesoft.project.seed.domain.exception.InvalidEmailException;
import net.iquesoft.project.seed.domain.exception.InvalidNameException;
import net.iquesoft.project.seed.domain.exception.InvalidPasswordException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegularExpressionValidationTest {
    RegularExpressionValidation validation = new RegularExpressionValidation();


    @Test
    public void testIsEmailValid() throws InvalidEmailException, EmptyFieldException {
        String s = "testing1.m_m2@email3.com4.ua5.net";
        boolean actualValue = validation.isEmailValid(s);
        assertEquals("Email " + s + " validation failed", true, actualValue);

    }

    @Test
    public void testIsNameValid() throws InvalidNameException, EmptyFieldException {
        String s = "Testing_Name123-abc";
        boolean actualValue = validation.isNameValid(s);
        assertEquals("Name " + s + " validation failed", true, actualValue);
    }

    @Test
    public void testIsPasswordValid() throws EmptyFieldException, InvalidPasswordException {
        String s = "Password123";
        boolean actualValue = validation.isPasswordValid(s);
        assertEquals("Password " + s + " validation failed", true, actualValue);
    }
}
