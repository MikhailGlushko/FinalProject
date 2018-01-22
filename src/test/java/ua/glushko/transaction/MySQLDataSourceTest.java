package ua.glushko.transaction;

import org.junit.Test;

import static org.junit.Assert.*;

public class MySQLDataSourceTest {

    @Test
    public void test(){
        ConnectionPool.getConnectionPool().setDataSource(MySQLDataSource.getDatasource());
    }

}