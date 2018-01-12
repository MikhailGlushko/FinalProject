package ua.glushko.services.impl;

import ua.glushko.configaration.MessageManager;
import ua.glushko.model.dao.GenericDAO;
import ua.glushko.model.dao.MySQLDAOFactory;
import ua.glushko.model.entity.RepairService;
import ua.glushko.model.exception.PersistException;
import ua.glushko.model.exception.TransactionException;
import ua.glushko.services.AbstractService;
import ua.glushko.transaction.TransactionManager;

import java.util.*;

public class RepairServicesService implements AbstractService {

    private RepairServicesService() {
    }

    public static RepairServicesService getService() {
        return new RepairServicesService();
    }

    public List<RepairService> getRepairServiceList() throws PersistException, TransactionException {
        List<RepairService> repairServiceList = Collections.emptyList();
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        try {
            TransactionManager.beginTransaction();
            repairServiceList = serviceDAO.read();
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return repairServiceList;
    }

    public List<RepairService> getRepairServiceList(int page, int pagesCount, int rowsPerPage) throws PersistException, TransactionException {
        List<RepairService> repairServiceList = Collections.emptyList();
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        int start = (page - 1) * rowsPerPage;
        int limit = pagesCount * rowsPerPage;
        try {
            TransactionManager.beginTransaction();
            repairServiceList = serviceDAO.read(start, limit);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return repairServiceList;
    }

    public List<String> getRepairServiceTitles() {
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        return  serviceDAO.getTableHead();
    }

    public RepairService getRepairServiceById(int id) throws PersistException, TransactionException {
        RepairService repairService = null;
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        try {
            TransactionManager.beginTransaction();
            repairService = serviceDAO.read(id);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
        return repairService;
    }

    public void updateRepairService(RepairService service) throws PersistException, TransactionException {
        if(service.getName()==null || service.getName().isEmpty()|| service.getNameRu()==null || service.getNameRu().isEmpty())
            throw new PersistException(MessageManager.getMessage("user.incorrectLoginOrPassword"));
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        try {
            TransactionManager.beginTransaction();
            serviceDAO.update(service);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }

    public void deleteRepairService(Integer serviceId) throws PersistException, TransactionException {
        GenericDAO<RepairService> serviceDAO = MySQLDAOFactory.getFactory().getRepairServiceDao();
        try {
            TransactionManager.beginTransaction();
            serviceDAO.delete(serviceId);
            TransactionManager.endTransaction();
        } finally {
            TransactionManager.rollBack();
        }
    }
}
