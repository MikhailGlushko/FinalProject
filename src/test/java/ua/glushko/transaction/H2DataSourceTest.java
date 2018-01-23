package ua.glushko.transaction;

import org.junit.Test;

import static org.junit.Assert.*;

public class H2DataSourceTest {

    @Test
    public void test(){
        ConnectionPool.getConnectionPool().setDataSource(H2DataSource.getInstance());
    }
}