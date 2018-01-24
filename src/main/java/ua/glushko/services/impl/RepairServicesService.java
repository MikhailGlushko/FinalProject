package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.dao.impl.RepairServiceDAO;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;
import ua.glushko.transaction.TransactionManager;

import java.sql.SQLException;
import java.util.*;

public class RepairServicesService extends Service {

    private RepairServicesService() {
    }

    public static RepairServicesService getService() {
        return new RepairServicesService();
    }

    /** List of RepairServices */
    public List<RepairService> getRepairServiceList() throws TransactionException, DatabaseException {
        return DAOFactory.getFactory().getRepairServiceDao().read();
    }

    /** List of repairServices with limit */
    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws TransactionException, DatabaseException {
        return DAOFactory.getFactory().getRepairServiceDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names */
    public List<String> getRepairServiceTitles() {
        return  DAOFactory.getFactory().getRepairServiceDao().getTableHead();
    }

    /** Get RepairService by id*/
    public RepairService getRepairServiceById(int id) throws DaoException {
        return getById(DAOFactory.getFactory().getRepairServiceDao(),id);
    }

    /** Update RepairService or create new */
    public void updateRepairService(RepairService service) throws TransactionException, DatabaseException {
        update(DAOFactory.getFactory().getRepairServiceDao(),service);
    }

    /** delete exist RepairService */
    public void deleteRepairService(Integer serviceId) throws TransactionException, DatabaseException {
        delete(DAOFactory.getFactory().getRepairServiceDao(),serviceId);
    }

    /** Total of repairServices */
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getRepairServiceDao());
    }
}
