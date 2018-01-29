package ua.glushko.transaction;

import org.junit.Test;

public class H2DataSourceTest {

    @Test
    public void test(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
    }
}