package ua.glushko.commands.impl.auth;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.COMMAND_NAME_USERS;
public class SetupSaveCommand extends AbstractCommand {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_NAME_LOCALE);
        try {
            updateUserDatasIntoDatabase(request);
            page = ConfigurationManager.getProperty(PATH_PAGE_INDEX);
            return new CommandRouter(request, response, page, CommandRouter.REDIRECT);
        } catch (NumberFormatException | NullPointerException | TransactionException | PersistException e){
            LOGGER.error(e);
            page = ConfigurationManager.getProperty(PATH_PAGE_USERS_SETUP);
            request.setAttribute(PARAM_NAME_ERROR_MESSAGE,
                    MessageManager.getMessage(MESSAGE_USER_INCORRECT_DATA, locale));
            return new CommandRouter(request, response, page);
        }
    }

    private void updateUserDatasIntoDatabase(HttpServletRequest request) throws PersistException, TransactionException {
        Integer userId = Integer.valueOf(request.getParameter(PARAM_NAME_USER_ID));
        UsersService usersService = UsersService.getService();
        User user = usersService.getUserById(userId);
        user = populateUserfromFequestData(request, user);
        usersService.updateUser(user);
        storeUserDataToSession(request,user);
    }

    private void storeUserDataToSession(HttpServletRequest request, User user){
        request.getSession().setAttribute(PARAM_NAME_USER, user);
        request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_USERS);
        request.getSession().setAttribute(PARAM_NAME_USER_LOGIN, user.getLogin());
        request.getSession().setAttribute(PARAM_NAME_USER_NAME, user.getName());
    }

    private User populateUserfromFequestData(HttpServletRequest request, User user) throws PersistException {
        String userPassword = request.getParameter(PARAM_NAME_USER_PASSWORD);
        String userPassword2 = request.getParameter(PARAM_NAME_USER_PASSWORD2);
        if(!userPassword.equals(userPassword2))
            throw new PersistException(MessageManager.getMessage("user.incorrectLoginOrPassword"));

        String userLogin = request.getParameter(PARAM_NAME_USER_LOGIN);
        String userName = request.getParameter(PARAM_NAME_USER_NAME);
        String userEmail = request.getParameter(PARAM_NAME_USER_EMAIL);
        String userPhone = request.getParameter(PARAM_NAME_USER_PHONE);

        user.setName(userName);
        user.setLogin(userLogin);
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setPhone(userPhone);
        return user;
    }
}
