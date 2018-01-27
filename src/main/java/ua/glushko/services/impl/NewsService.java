package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.NewsDAO;
import ua.glushko.model.entity.News;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.List;

public class NewsService extends Service {

    private NewsService() {
    }

    public static NewsService getService() {
        return new NewsService();
    }

    /** List of News*/
    public List<News> getNewsList() throws DatabaseException {
        return DAOFactory.getFactory().getNewsDAO().read();
    }

    /** List of News with limit */
    public List<News> getNewsList(int page, int pagesCount, int rowsPerPage) throws DatabaseException {
        return DAOFactory.getFactory().getNewsDAO().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names */
    public List<String> getNewsTitles() {
        return  DAOFactory.getFactory().getNewsDAO().getTableHead();
    }

    /** Get News by Id*/
    public News getNewsId(int id) throws DaoException {
        return getById(DAOFactory.getFactory().getNewsDAO(),id);
    }

    /** Update exist News or create new */
    public void updateNews(News item) throws TransactionException, DatabaseException {
        update(DAOFactory.getFactory().getNewsDAO(),item);
    }

    /** Delete exist News*/
    public void deleteNews(Integer newsId) throws TransactionException, DatabaseException {
        delete(DAOFactory.getFactory().getNewsDAO(),newsId);
    }

    /** Total of News */
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getNewsDAO());
    }

    /** Total of News*/
    public int count(int id) throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getNewsDAO(),id);
    }
}
