package ua.glushko.commands;

import org.apache.log4j.Logger;
import ua.glushko.commands.impl.*;
import ua.glushko.commands.impl.auth.*;
import ua.glushko.commands.impl.services.ServiceSaveCommand;
import ua.glushko.commands.impl.services.ServicesCommand;
import ua.glushko.commands.impl.services.ServiceDetailCommand;
import ua.glushko.commands.impl.users.UserDetailCommand;
import ua.glushko.commands.impl.users.UserSaveCommand;
import ua.glushko.commands.impl.users.UsersCommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ua.glushko.commands.AbstractCommand.*;

public class CommandFactory {
    public static final String COMMAND_NAME_LOGIN = "login";
    public static final String COMMAND_NAME_REGISTER = "register";
    public static final String COMMAND_NAME_RESET_PASSWORD = "resetpassword";
    public static final String COMMAND_NAME_LOGOUT = "logout";
    // for users setup
    public static final String COMMAND_NAME_USERS = "users";
    public static final String COMMAND_NAME_USERS_DETAIL = "users_detail";
    public static final String COMMAND_NAME_USERS_SAVE = "users_save";
    // for repair services setup
    public static final String COMMAND_NAME_SERVICES = "services";
    public static final String COMMAND_NAME_SERVICES_DETAIL = "services_detail";
    public static final String COMMAND_NAME_SERVICES_SAVE = "services_save";

    public static final String COMMAND_NAME_SETUP = "setup";
    public static final String COMMAND_NAME_SETUP_SAVE = "setup_save";
    public static final String COMMAND_NAME_WELCOME = "welcome";
    public static final String COMMAND_NAME_LANG = "lang";

    private static final Logger logger = Logger.getLogger(CommandFactory.class.getSimpleName());
    private static final CommandFactory COMMAND_FACTORY = new CommandFactory();

    private final Map<String, Command> commandMap = new HashMap<>();

    private CommandFactory() {
        commandMap.put(COMMAND_NAME_LOGIN, new LoginCommand());
        commandMap.put(COMMAND_NAME_LOGOUT, new LogoutCommand());
        commandMap.put(COMMAND_NAME_WELCOME, new WelcomeCommand());
        commandMap.put(COMMAND_NAME_LANG, new SwitchLanguageCommand());

        commandMap.put(null, new ErrorCommand());
        commandMap.put(COMMAND_NAME_REGISTER, new RegisterCommand());

        commandMap.put(COMMAND_NAME_USERS, new UsersCommand());
        commandMap.put(COMMAND_NAME_USERS_DETAIL, new UserDetailCommand());
        commandMap.put(COMMAND_NAME_USERS_SAVE, new UserSaveCommand());

        commandMap.put(COMMAND_NAME_SERVICES, new ServicesCommand());
        commandMap.put(COMMAND_NAME_SERVICES_DETAIL, new ServiceDetailCommand());
        commandMap.put(COMMAND_NAME_SERVICES_SAVE, new ServiceSaveCommand());

        commandMap.put(COMMAND_NAME_SETUP, new SetupCommand());
        commandMap.put(COMMAND_NAME_SETUP_SAVE, new SetupSaveCommand());
        commandMap.put(COMMAND_NAME_RESET_PASSWORD, new ResetPasswordCommand());
    }

    /**
     * получаем екземпляр фабрики
     */
    public static CommandFactory getInstance() {
        return COMMAND_FACTORY;
    }

    /**
     * Получаем комманду из запроса
     */
    public Command getCommand(HttpServletRequest req) {
        Command emptyCommand = new EmptyCommand();
        String command;
        Object attribute = req.getAttribute(PARAM_NAME_COMMAND);
        if(Objects.nonNull(attribute))
            command = (String) attribute;
        else
            command = req.getParameter(PARAM_NAME_COMMAND);
        logger.debug("Getting command: " + command);
        Command newCommand;
        if (command == null || command.isEmpty() || (newCommand = commandMap.get(command)) == null)
            return emptyCommand;
        return newCommand;
    }
}
