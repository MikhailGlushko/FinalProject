package ua.glushko.services.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.dao.impl.RepairServiceDAO;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
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

    public List<RepairService> getRepairServiceList() throws PersistException, TransactionException {
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

    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        RepairServiceDAO repairServiceDao = MySQLDAOFactory.getFactory().getRepairServiceDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        List<RepairService> read;
        try {
            TransactionManager.beginTransaction();
            read = repairServiceDao.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return read;
    }

    public List<String> getRepairServiceTitles() {
        return  MySQLDAOFactory.getFactory().getRepairServiceDao().getTableHead();
    }

    public RepairService getRepairServiceById(int id) throws PersistException, TransactionException {
        return getById(MySQLDAOFactory.getFactory().getRepairServiceDao(),id);
    }

    public void updateRepairService(RepairService service) throws PersistException, TransactionException {
        update(MySQLDAOFactory.getFactory().getRepairServiceDao(),service);
    }

    public void deleteRepairService(Integer serviceId) throws PersistException, TransactionException {
        delete(MySQLDAOFactory.getFactory().getRepairServiceDao(),serviceId);
    }

    public int count() throws SQLException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getRepairServiceDao());
    }
}
