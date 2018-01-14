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

import java.util.*;

public class RepairServicesService extends AbstractService {

    private RepairServicesService() {
    }

    public static RepairServicesService getService() {
        return new RepairServicesService();
    }

    public List<RepairService> getRepairServiceList() throws PersistException, TransactionException {
        return (List<RepairService>) getList(MySQLDAOFactory.getFactory().getRepairServiceDao());
    }

    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        return (List<RepairService>) getList(MySQLDAOFactory.getFactory().getRepairServiceDao(),page,pagesCount,rowsPerPage);
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

    public int count() throws PersistException, TransactionException {
        return this.count(MySQLDAOFactory.getFactory().getRepairServiceDao());
    }
}
