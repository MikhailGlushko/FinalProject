package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.transaction.H2DataSource;
import ua.glushko.model.entity.RepairService;
import ua.glushko.exception.DaoException;
import ua.glushko.exception.DatabaseException;
import ua.glushko.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class RepairServicesServiceTest {

    private RepairServicesService service;
    @Before
    @Test
    public void getService() {
        service = RepairServicesService.getService();
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
    }

    @Test
    public void getRepairServiceList() throws DatabaseException {
        List<RepairService> repairServiceList = service.getRepairServiceList();
        assertNotNull(repairServiceList);
    }

    @Test
    public void getRepairServiceList1() throws DatabaseException {
        List<RepairService> repairServiceList = service.getRepairServiceList(1, 1, 1);
        assertNotNull(repairServiceList);
    }

    @Test
    public void getRepairServiceTitles() throws DatabaseException {
        service.getRepairServiceList(1, 1, 1);
        List<String> serviceTitles = service.getRepairServiceTitles();
        assertNotNull(serviceTitles);
    }

    @Test
    public void getRepairServiceById() throws DaoException {
        RepairService repairServiceById = service.getRepairServiceById(1);
        assertNotNull(repairServiceById);
    }

    @Test
    public void updateRepairService() throws TransactionException, DatabaseException {
        RepairService repairServiceById = service.getRepairServiceById(1);
        String name = repairServiceById.getName();
        repairServiceById.setName(name+"!");
        service.updateRepairService(repairServiceById);
        RepairService updated = service.getRepairServiceById(1);
        String updatedName = updated.getName();
        assertNotEquals(updatedName,name);
    }

    @Test
    public void deleteRepairService() throws TransactionException, DatabaseException {
        service.deleteRepairService(2);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
    }
}