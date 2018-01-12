package ua.glushko.commands.impl.auth;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.model.entity.User;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SetupCommand extends AbstractCommand {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        HttpSession session = request.getSession();
        String userLogin = (String) session.getAttribute(AbstractCommand.PARAM_NAME_USER_LOGIN);
        if(userLogin!=null) {
            User user;
            UsersService usersService = UsersService.getService();
            try {
                user = usersService.getUserByLogin(userLogin);
                session.setAttribute(PARAM_NAME_USER, user);
            } catch (PersistException | TransactionException e) {
                LOGGER.error(e);
            }
        }

        page = ConfigurationManager.getProperty(PATH_PAGE_USERS_SETUP);
        return new CommandRouter(request, response, page);
    }
}