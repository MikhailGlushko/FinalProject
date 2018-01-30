package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.News;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * News Services
 * @author Mikhail Glushko
 * @version 1.0
 */
public class NewsService extends Service {

    private NewsService() {
    }

    public static NewsService getService() {
        return new NewsService();
    }

    /** List of News with limit */
    public List<News> getNewsList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getNewsDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }
}
