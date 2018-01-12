package ua.glushko.configaration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static ua.glushko.commands.CommandFactory.*;

public class SecurityConfiguraton {
    private static final SecurityConfiguraton config = new SecurityConfiguraton();

    private final Map<String, String> grant = new HashMap<>();

    public static SecurityConfiguraton getInstance() {
        return config;
    }

    private SecurityConfiguraton() {
        grant.put(COMMAND_NAME_LOGIN, "ALL");
        grant.put(COMMAND_NAME_REGISTER, "ALL");
        grant.put(COMMAND_NAME_LOGOUT, "AUTH");
        grant.put(COMMAND_NAME_USERS, "AUTH");
        grant.put("/", "ALL");
    }

    public String security(String command) {
        return grant.get(command);
    }

    public Set<String> getEndPoints() {
        return grant.keySet();
    }
}
