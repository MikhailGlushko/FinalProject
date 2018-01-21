package ua.glushko.services.impl;

import org.junit.Before;
import org.junit.Test;
import ua.glushko.model.dao.H2DataSource;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.transaction.ConnectionPool;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class RepairServicesServiceTest {

    RepairServicesService service;
    @Before
    @Test
    public void getService() {
        service = RepairServicesService.getService();
        if (ConnectionPool.getConnectionPool().getDataSource() == null)
            ConnectionPool.getConnectionPool().setDataSource(H2DataSource.H2_CONNECTION_POOL);
    }

    @Test
    public void getRepairServiceList() throws PersistException, TransactionException {
        List<RepairService> repairServiceList = service.getRepairServiceList();
        assertNotNull(repairServiceList);
    }

    @Test
    public void getRepairServiceList1() throws PersistException, TransactionException {
        List<RepairService> repairServiceList = service.getRepairServiceList(1, 1, 1);
        assertNotNull(repairServiceList);
    }

    @Test
    public void getRepairServiceTitles() throws PersistException, TransactionException {
        service.getRepairServiceList(1, 1, 1);
        List<String> serviceTitles = service.getRepairServiceTitles();
        assertNotNull(serviceTitles);
    }

    @Test
    public void getRepairServiceById() throws PersistException, TransactionException {
        RepairService repairServiceById = service.getRepairServiceById(1);
        assertNotNull(repairServiceById);
    }

    @Test
    public void updateRepairService() throws PersistException, TransactionException {
        RepairService repairServiceById = service.getRepairServiceById(1);
        String name = repairServiceById.getName();
        repairServiceById.setName(name+"!");
        service.updateRepairService(repairServiceById);
        RepairService updated = service.getRepairServiceById(1);
        String updatedName = updated.getName();
        assertNotEquals(updatedName,name);
    }

    @Test
    public void deleteRepairService() throws PersistException, TransactionException {
        service.deleteRepairService(2);
    }

    @Test
    public void count() throws SQLException, TransactionException {
        int count = service.count();
        assertTrue(count!=0);
    }
}