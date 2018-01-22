package ua.glushko.services;

import org.junit.Test;
import ua.glushko.services.utils.Validator;

import static org.junit.Assert.*;

public class ValidatorTest {

    @Test
    public void matchLogin() {
        boolean b1;
        b1 = Validator.validateLogin("user");
        assertFalse(b1);
        b1 = Validator.validateLogin("user1234@");
        assertFalse(b1);
        b1 = Validator.validateLogin("user.name");
        assertTrue(b1);
        b1 = Validator.validateLogin("user_name");
        assertTrue(b1);
    }

    @Test
    public void matchPassword() {
        boolean b1;
        b1 = Validator.validatePassword("compaq");
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q");
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@ q00");
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@<div>q00");
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q&nbsp;01");
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q0101");
        assertTrue(b1);
    }

    @Test
    public void matchEmail(){
        boolean b1 = Validator.validateEmail("test");
        assertFalse(b1);
        b1 = Validator.validateEmail("test@test");
        assertFalse(b1);
        b1 = Validator.validateEmail("test@test.com");
        assertTrue(b1);
    }
}