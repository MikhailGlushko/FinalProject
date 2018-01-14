package ua.glushko.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface GenericCommand {
    CommandRouter execute(HttpServletRequest request, HttpServletResponse response);
}
