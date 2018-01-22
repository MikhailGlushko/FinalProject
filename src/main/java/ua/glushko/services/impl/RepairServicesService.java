package ua.glushko.services.impl;

import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.RepairServiceDAO;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.*;

public class RepairServicesService extends AbstractService {

    private RepairServicesService() {
    }

    public static RepairServicesService getService() {
        return new RepairServicesService();
    }

    public List<RepairService> getRepairServiceList() throws TransactionException, DatabaseException {
        RepairServiceDAO repairServiceDao = MySQLDAOFactory.getFactory().getRepairServiceDao();
        List<RepairService> read;
        try{
            TransactionManager.beginTransaction();
            read = repairServiceDao.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
        RepairServiceDAO repairServiceDao = MySQLDAOFactory.getFactory().getRepairServiceDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<RepairService> read = null;
        try {
            TransactionManager.beginTransaction();
            read = repairServiceDao.read(start, limit);
            TransactionManager.endTransaction();
        } catch (DatabaseException e) {
            e.printStackTrace();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getRepairServiceTitles() {
        return  MySQLDAOFactory.getFactory().getRepairServiceDao().getTableHead();
    }

    public RepairService getRepairServiceById(int id) throws DaoException {
        return getById(MySQLDAOFactory.getFactory().getRepairServiceDao(),id);
    }

    public void updateRepairService(RepairService service) throws TransactionException, DatabaseException {
        update(MySQLDAOFactory.getFactory().getRepairServiceDao(),service);
    }

    public void deleteRepairService(Integer serviceId) throws TransactionException, DatabaseException {
        delete(MySQLDAOFactory.getFactory().getRepairServiceDao(),serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getRepairServiceDao());
    }
}
