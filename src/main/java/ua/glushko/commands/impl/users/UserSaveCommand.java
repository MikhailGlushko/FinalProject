package ua.glushko.commands.impl.users;

import ua.glushko.authentification.Authentification;
import ua.glushko.commands.AbstractCommand;
import ua.glushko.commands.CommandRouter;
import ua.glushko.model.entity.Grant;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.impl.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static ua.glushko.authentification.Authentification.D;
import static ua.glushko.authentification.Authentification.U;
import static ua.glushko.commands.CommandFactory.*;

public class UserSaveCommand extends AbstractCommand {
    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {

        String page;
        try {
            storeUserDatasToDatabase(request);
        }catch (TransactionException | PersistException e) {
            LOGGER.error(e);
        }
        page = "/do?command="+COMMAND_NAME_USERS;
        return new CommandRouter(request, response, page, CommandRouter.REDIRECT);

    }

    private void storeUserDatasToDatabase(HttpServletRequest request) throws PersistException, TransactionException{
        try{
            String button = request.getParameter("button");
            HttpSession session = request.getSession();
            List<Grant> grantList = (List<Grant>) session.getAttribute(PARAM_NAME_USER_GRANTS);
            UserRole userRole = (UserRole) session.getAttribute(PARAM_NAME_USER_ROLE);
            String command = request.getParameter(PARAM_NAME_COMMAND);
            int access = Authentification.checkAccess(grantList,userRole,command);
            if("save".equals(button)) {
                Integer userId = Integer.valueOf(request.getParameter(PARAM_NAME_USER_ID));
                String userName = request.getParameter(PARAM_NAME_USER_NAME);
                String userEmail = request.getParameter(PARAM_NAME_USER_EMAIL);
                String userPhone = request.getParameter(PARAM_NAME_USER_PHONE);
                String userStatus = request.getParameter(PARAM_NAME_USER_STATUS);
                UsersService usersService = UsersService.getService();
                // get user data drom database
                User user = usersService.getUserById(userId);
                user.setId(userId);
                user.setRole(userRole);
                user.setName(userName);
                user.setEmail(userEmail);
                user.setPhone(userPhone);
                user.setStatus(userStatus);
                if ((access & U) == U) {
                    LOGGER.debug("updating user "+userId);
                    // update user data into database
                    usersService.updateUser(user);
                }
                session.setAttribute(PARAM_NAME_USER, user);
                request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_USERS);
            } else {
                Integer userId = Integer.valueOf(request.getParameter(PARAM_NAME_USER_ID));
                UsersService usersService = UsersService.getService();
                if ((access & D) == D) {
                    LOGGER.debug("deleting user "+userId);
                    // update user data into database
                    usersService.deleteUser(userId);
                }
                request.setAttribute(PARAM_NAME_COMMAND, COMMAND_NAME_USERS);
            }
        } catch (NullPointerException | NumberFormatException e){
            LOGGER.error(e);
        }
    }
}
