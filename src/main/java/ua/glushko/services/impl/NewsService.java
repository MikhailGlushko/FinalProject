package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.GuestBookDAO;
import ua.glushko.model.dao.impl.NewsDAO;
import ua.glushko.model.entity.GuestBook;
import ua.glushko.model.entity.News;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.List;

public class NewsService extends AbstractService {

    private NewsService() {
    }

    public static NewsService getService() {
        return new NewsService();
    }

    public List<News> getNewsList() throws PersistException, TransactionException {
        NewsDAO newsDAO = MySQLDAOFactory.getFactory().getNewsDAO();
        List<News> read;
        try{
            TransactionManager.beginTransaction();
            read = newsDAO.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<News> getNewsList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        NewsDAO newsDAO = MySQLDAOFactory.getFactory().getNewsDAO();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<News> read;
        try {
            TransactionManager.beginTransaction();
            read = newsDAO.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getNewsTitles() {
        return  MySQLDAOFactory.getFactory().getNewsDAO().getTableHead();
    }

    public News getNewsId(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getNewsDAO(),id);
    }

    public void updateNews(News item) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getNewsDAO(),item);
    }

    public void deleteNews(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getNewsDAO(),serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getNewsDAO());
    }

    public int count(int id) throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getNewsDAO(),id);
    }
}
