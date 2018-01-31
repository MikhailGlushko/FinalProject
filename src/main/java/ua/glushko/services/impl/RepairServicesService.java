package ua.glushko.services.impl;

import ua.glushko.model.dao.DAOFactory;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.services.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * repairServices services
 * @author Mikhail Glushko
 * @version 1.0
 */
public class RepairServicesService extends Service {

    private RepairServicesService() {
    }

    public static RepairServicesService getService() {
        return new RepairServicesService();
    }

    /** List of RepairServices */
    public List<RepairService> getRepairServiceList() throws SQLException {
        return DAOFactory.getFactory().getRepairServiceDao().read();
    }

    /** List of repairServices with limit */
    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws SQLException {
        return DAOFactory.getFactory().getRepairServiceDao().read((page - 1) * rowsPerPage, pagesCount * rowsPerPage);
    }

    /** List of field names */
    public List<String> getRepairServiceTitles() {
        return  DAOFactory.getFactory().getRepairServiceDao().getTableHead();
    }

    /** Get RepairService by id*/
    public RepairService getRepairServiceById(int id) throws SQLException {
        return getById(DAOFactory.getFactory().getRepairServiceDao(),id);
    }

    /** Update RepairService or create new */
    public void updateRepairService(RepairService service) throws TransactionException, SQLException {
        update(DAOFactory.getFactory().getRepairServiceDao(),service);
    }

    /** delete exist RepairService */
    public void deleteRepairService(Integer serviceId) throws TransactionException, SQLException {
        delete(DAOFactory.getFactory().getRepairServiceDao(),serviceId);
    }

    /** Total of repairServices */
    public int count() throws SQLException, TransactionException {
        return this.count(DAOFactory.getFactory().getRepairServiceDao());
    }
}
