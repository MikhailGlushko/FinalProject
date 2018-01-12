package ua.glushko.services;

import org.junit.Test;

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
        System.out.println(b1);
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q");
        System.out.println(b1);
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@ q00");
        System.out.println(b1);
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@<div>q00");
        System.out.println(b1);
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q&nbsp;01");
        System.out.println(b1);
        assertFalse(b1);
        b1 = Validator.validatePassword("Comp@q0101");
        System.out.println(b1);
        assertTrue(b1);
        System.out.println(b1);
    }
}