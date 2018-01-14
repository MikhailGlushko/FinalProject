package ua.glushko.commands.impl.admin.services;

import ua.glushko.commands.Command;
import ua.glushko.commands.CommandRouter;
import ua.glushko.commands.impl.admin.users.UsersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** перенаправление на форму добавления нового пользователя при нажатии кнопки "+" на форме со списком пользователей*/
public class ServiceAddCommand extends Command {

    @Override
    public CommandRouter execute(HttpServletRequest request, HttpServletResponse response) {
        String page = ConfigurationManager.getProperty(ServicesCommandHelper.PATH_PAGE_SERVICE_ADD);
        return new CommandRouter(request, response, page);
    }
}