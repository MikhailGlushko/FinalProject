package ua.glushko.commands.impl.admin.setup;
import ua.glushko.commands.CommandFactory;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.Command;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.User;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.ParameterException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static ua.glushko.commands.CommandFactory.COMMAND_USERS;
import static ua.glushko.services.utils.Validator.getValidatedUserBeforeSetup;

/** Saving user credentials after change */
public class SetupSaveCommand implements Command {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String locale = (String) request.getSession().getAttribute(PARAM_LOCALE);
        User userNew;
        try {
            userNew = getValidatedUserBeforeSetup(request);
            UsersService usersService = UsersService.getService();
            User userOld = usersService.getUserById(userNew.getId());
            populateUser(userNew,userOld);
            usersService.updateUser(userOld);
            storeUserDataToSession(request,userOld);
            request.setAttribute(PARAM_COMMAND, CommandFactory.COMMAND_WELCOME);
            page = "/do";
        } catch (TransactionException | DatabaseException e){
            LOGGER.error(e);
            page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
            request.setAttribute(PARAM_ERROR_MESSAGE,
                    MessageManager.getMessage(UsersCommandHelper.MESSAGE_USER_INCORRECT_DATA, locale));
        } catch (ParameterException e) {
            page = ConfigurationManager.getProperty(UsersCommandHelper.PATH_PAGE_USERS_SETUP);
            request.setAttribute(PARAM_ERROR_MESSAGE,
                    MessageManager.getMessage(e.getMessage(), locale));
        }
            return new CommandRouter(request, response, page);
    }


    private void storeUserDataToSession(HttpServletRequest request, User user){
        request.setAttribute(PARAM_COMMAND, COMMAND_USERS);
        request.getSession().setAttribute(UsersCommandHelper.PARAM_NAME_USER, user);
        request.getSession().setAttribute(UsersCommandHelper.PARAM_USER_LOGIN, user.getLogin());
        request.getSession().setAttribute(UsersCommandHelper.PARAM_USER_NAME, user.getName());
    }

    private void populateUser(User userFrom, User userTo){
        userTo.setLogin(userFrom.getLogin());
        userTo.setName(userFrom.getName());
        userTo.setPassword(userFrom.getPassword());
        userTo.setEmail(userFrom.getEmail());
        userTo.setPhone(userFrom.getPhone());
    }
}
