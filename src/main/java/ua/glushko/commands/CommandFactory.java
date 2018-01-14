package ua.glushko.commands;

import org.apache.log4j.Logger;
import ua.glushko.commands.impl.*;
import ua.glushko.commands.impl.admin.orders.*;
import ua.glushko.commands.impl.admin.services.*;
import ua.glushko.commands.impl.admin.users.*;
import ua.glushko.commands.impl.auth.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ua.glushko.commands.Command.*;

public class CommandFactory {
    public static final String COMMAND_NAME_LOGIN = "login";
    public static final String COMMAND_NAME_REGISTER = "register";
    public static final String COMMAND_NAME_RESET_PASSWORD = "resetpassword";
    public static final String COMMAND_NAME_LOGOUT = "logout";
    // for users setup
    public static final String COMMAND_NAME_USERS = "users";
    public static final String COMMAND_NAME_USERS_READ = "users_detail";
    public static final String COMMAND_NAME_USERS_ACTION_CRUD = "users_action";
    public static final String COMMAND_NAME_USERS_ADD = "users_add";
    public static final String COMMAND_NAME_USERS_UPDATE = "users_update";
    public static final String COMMAND_NAME_USERS_DELETE = "users_delete";
    public static final String COMMAND_NAME_USERS_CREATE = "users_create";
    // for repair services setup
    public static final String COMMAND_NAME_SERVICES = "services";
    public static final String COMMAND_NAME_SERVICES_READ = "services_detail";
    public static final String COMMAND_NAME_SERVICES_ACTION_CRUD = "services_action";
    public static final String COMMAND_NAME_SERVICES_ADD = "repairservices_add";
    public static final String COMMAND_NAME_SERVICES_UPDATE = "services_update";
    public static final String COMMAND_NAME_SERVICES_DELETE = "services_delete";
    public static final String COMMAND_NAME_SERVICES_CREATE = "services_create";
    // for requests setup
    public static final String COMMAND_NAME_ORDERS = "orders";
    public static final String COMMAND_NAME_ORDERS_READ = "orders_detail";
    public static final String COMMAND_NAME_ORDERS_ACTION_CRUD = "orders_action";
    public static final String COMMAND_NAME_ORDERS_ADD = "orders_add";
    public static final String COMMAND_NAME_ORDERS_UPDATE = "orders_update";
    public static final String COMMAND_NAME_ORDERS_DELETE = "orders_delete";
    public static final String COMMAND_NAME_ORDERS_CREATE = "orders_create";

    public static final String COMMAND_NAME_SETUP = "setup";
    public static final String COMMAND_NAME_SETUP_SAVE = "setup_save";
    public static final String COMMAND_NAME_WELCOME = "welcome";
    public static final String COMMAND_NAME_LANG = "lang";

    private static final Logger logger = Logger.getLogger(CommandFactory.class.getSimpleName());
    private static final CommandFactory COMMAND_FACTORY = new CommandFactory();

    private final Map<String, GenericCommand> commandMap = new HashMap<>();

    private CommandFactory() {
        commandMap.put(COMMAND_NAME_LOGIN, new LoginCommand());
        commandMap.put(COMMAND_NAME_LOGOUT, new LogoutCommand());
        commandMap.put(COMMAND_NAME_WELCOME, new WelcomeCommand());
        commandMap.put(COMMAND_NAME_LANG, new SwitchLanguageCommand());

        commandMap.put(null, new ErrorCommand());
        commandMap.put(COMMAND_NAME_REGISTER, new RegisterCommand());

        commandMap.put(COMMAND_NAME_USERS, new UsersListCommand());
        commandMap.put(COMMAND_NAME_USERS_ADD, new UserAddCommand());
        commandMap.put(COMMAND_NAME_USERS_ACTION_CRUD, new UserActionCRUDCommand());
        commandMap.put(COMMAND_NAME_USERS_CREATE, new UserCreateCommand());
        commandMap.put(COMMAND_NAME_USERS_READ,   new UserReadCommand());
        commandMap.put(COMMAND_NAME_USERS_UPDATE, new UserUpdateCommand());
        commandMap.put(COMMAND_NAME_USERS_DELETE, new UserDeleteCommand());

        commandMap.put(COMMAND_NAME_SERVICES, new ServicesListCommand());
        commandMap.put(COMMAND_NAME_SERVICES_ADD, new ServiceAddCommand());
        commandMap.put(COMMAND_NAME_SERVICES_ACTION_CRUD, new ServiceActionCRUDCommand());
        commandMap.put(COMMAND_NAME_SERVICES_CREATE, new ServiceCreateCommand());
        commandMap.put(COMMAND_NAME_SERVICES_READ,   new ServiceReadCommand());
        commandMap.put(COMMAND_NAME_SERVICES_UPDATE, new ServiceUpdateCommand());
        commandMap.put(COMMAND_NAME_SERVICES_DELETE, new ServiceDeleteCommand());

        commandMap.put(COMMAND_NAME_ORDERS, new OrdersListCommand());
        commandMap.put(COMMAND_NAME_ORDERS_ADD, new OrderAddCommand());
        commandMap.put(COMMAND_NAME_ORDERS_ACTION_CRUD, new OrderActionCRUDCommand());
        commandMap.put(COMMAND_NAME_ORDERS_CREATE, new OrderCreateCommand());
        commandMap.put(COMMAND_NAME_ORDERS_READ,   new OrderReadCommand());
        commandMap.put(COMMAND_NAME_ORDERS_UPDATE, new OrderUpdateCommand());
        commandMap.put(COMMAND_NAME_ORDERS_DELETE, new OrderDeleteCommand());

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
    public GenericCommand getCommand(HttpServletRequest req) {
        GenericCommand emptyCommand = new EmptyCommand();
        String command;
        Object attribute = req.getAttribute(PARAM_NAME_COMMAND);
        if(Objects.nonNull(attribute))
            command = (String) attribute;
        else
            command = req.getParameter(PARAM_NAME_COMMAND);
        logger.debug("Getting command: " + command);
        GenericCommand newCommand;
        if (command == null || command.isEmpty() || (newCommand = commandMap.get(command)) == null)
            return emptyCommand;
        return newCommand;
    }
}
