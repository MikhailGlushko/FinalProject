package ua.glushko.model.entity;

import java.io.Serializable;

/**
 * Roles grants
 * @author Mikhail Glushko
 * @version 1.0
 */
public class Grant implements GenericEntity, Serializable {
    private int id;
    private String command;
    private String menu;
    private UserRole role;
    private String action;
    private Scope scope = Scope.NONE;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public UserRole getRole() {
        return role;
    }

    @SuppressWarnings("unused")
    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Scope getScope() {
        return scope;
    }

    @SuppressWarnings("unused")
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "Grant{" +
                "id=" + id +
                ", command='" + command + '\'' +
                ", menu='" + menu + '\'' +
                ", role=" + role +
                ", action='" + action + '\'' +
                ", scope=" + scope +
                '}';
    }

    public void setRole(String role) {
        this.role = UserRole.valueOf(UserRole.class, role);
    }

    public void setScope(String scope) {
        this.scope = Scope.valueOf(Scope.class, scope);
    }
}
