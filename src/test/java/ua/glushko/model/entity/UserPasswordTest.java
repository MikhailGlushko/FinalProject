package ua.glushko.model.entity;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UserPasswordTest {

    private static final Logger logger = Logger.getLogger(UserPasswordTest.class.getSimpleName());

    @Parameter()
    public String password;
    @Parameter(value = 1)
    public String result;

    @Parameters
    public static Collection getParameters(){
        return Arrays.asList(new Object[][]{
                {"password",null},
                {"P@ssw0rd","P@ssw0rd"},
                {"P@sw0rd",null},
                {null,null},
                {"",null}
        });
    }

    @Test
    public void setPassword() {
        try {
            User user = new User();
            user.setPassword(password);
            result = user.getPassword();
            assertEquals(password, result);
        } catch (IllegalArgumentException e){
            logger.error(e);
        }
    }
}