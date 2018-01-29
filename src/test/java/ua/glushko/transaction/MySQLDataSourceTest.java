package ua.glushko.transaction;

import org.junit.Test;

public class MySQLDataSourceTest {

    @Test
    public void test(){
        ConnectionPool.getConnectionPool().setDataSource(MySQLDataSource.getDatasource());
    }

}