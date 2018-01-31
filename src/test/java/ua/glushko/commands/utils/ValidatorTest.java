package ua.glushko.commands.utils;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.UserDAO;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.entity.UserStatus;
import ua.glushko.transaction.ConnectionPool;
import ua.glushko.transaction.H2DataSource;

import javax.servlet.http.HttpServletRequest;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidatorTest {

    @Before
    public void init()    {
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
    }

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

    @Test(expected = ParameterException.class)
    public void getValidatedUserBeforeSetup() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());
        UsersCommandHelper.prepareUserDataBeforeSetup(request);
    }

    @Test
    public void getValidatedUserBeforeSetup2() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        tmp.setPassword("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());
        UsersCommandHelper.prepareUserDataBeforeSetup(request);
    }

    @Test (expected = ParameterException.class)
    public void getValidatedUserBeforeCreate() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        tmp.setPassword("P@ssw0rd");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());

        when(request.getParameter(UsersCommandHelper.PARAM_USER_STATUS)).thenReturn(tmp.getStatus().name());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ROLE)).thenReturn(tmp.getRole().name());
        UsersCommandHelper.prepareUserDataBeforeCreate(request);
    }

    @Test
    public void getValidatedUserBeforeCreate2() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        tmp.setPassword("P@ssw0rd");
        tmp.setLogin("administrator");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_STATUS)).thenReturn(tmp.getStatus().name());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ROLE)).thenReturn(tmp.getRole().name());
        UsersCommandHelper.prepareUserDataBeforeCreate(request);
    }

    @Test(expected = ParameterException.class)
    public void getValidatedUserBeforePasswordChange() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        UsersCommandHelper.getValidatedUserBeforePasswordChange(request);
    }

    @Test
    public void getValidatedUserBeforePasswordChange2() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        tmp.setPassword("P@ssw0rd");
        tmp.setLogin("administrator");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_LOGIN)).thenReturn(tmp.getLogin());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD)).thenReturn(tmp.getPassword());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PASSWORD2)).thenReturn(tmp.getPassword());
        UsersCommandHelper.getValidatedUserBeforePasswordChange(request);
    }

    @Test (expected = ParameterException.class)
    public void getValidatedUserBeforeUpdateDetails() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        tmp.setEmail("email");
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_STATUS)).thenReturn(tmp.getStatus().name());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ROLE)).thenReturn(tmp.getRole().name());
        UsersCommandHelper.getUserDataBeforeUpdate(request);
    }

    @Test
    public void getValidatedUserBeforeUpdateDetails2() throws SQLException, ParameterException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserDAO userDAO = DAOFactory.getFactory().getUserDao();
        User tmp = userDAO.read(1);
        assertNotNull(tmp);
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ID)).thenReturn(tmp.getId().toString());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_NAME)).thenReturn(tmp.getName());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_EMAIL)).thenReturn(tmp.getEmail());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_PHONE)).thenReturn(tmp.getPhone());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_STATUS)).thenReturn(tmp.getStatus().name());
        when(request.getParameter(UsersCommandHelper.PARAM_USER_ROLE)).thenReturn(tmp.getRole().name());
        UsersCommandHelper.getUserDataBeforeUpdate(request);
    }

    @Test
    public void validateUserId(){
        boolean b = Validator.validateUserId("");
        assertFalse(b);
        b = Validator.validateUserId("1");
        assertTrue(b);
    }

    @Test
    public void validateUserStatus(){
        boolean b = Validator.validateUserStatus("");
        assertFalse(b);
        b = Validator.validateUserStatus(UserStatus.ACTIVE.name());
        assertTrue(b);
    }

    @Test
    public void validateUserRole(){
        boolean b = Validator.validateUserRole("");
        assertFalse(b);
        b = Validator.validateUserRole(UserRole.MANAGER.name());
        assertTrue(b);
    }
}