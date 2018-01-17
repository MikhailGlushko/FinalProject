package ua.glushko.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ICommand {
    CommandRouter execute(HttpServletRequest request, HttpServletResponse response);
}
