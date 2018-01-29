package ua.glushko.model.entity;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserLoginTest {

    private static final Logger logger = Logger.getLogger(UserLoginTest.class.getSimpleName());

    @Parameter()
    public String login;
    @Parameter(value = 1)
    public String result;

    @Parameters
    public static Collection getParameters(){
        return Arrays.asList(new Object[][]{
                {"authenticateUser","authenticateUser"},
                {"new authenticateUser",null},
                {"1login",null},
                {"login1","login1"},
                {"authenticateUser.authenticateUser","authenticateUser.authenticateUser"},
                {"login_login","login_login"},
                {"authenticateUser@",null},
                {null,null},
                {"",null}
        });
    }

    @Test
    public void setLogin() {
        try {
            User user = new User();
            user.setLogin(login);
            result = user.getLogin();
            assertEquals(login, result);
        } catch (IllegalArgumentException e){
            logger.error(e);
        }
    }

}