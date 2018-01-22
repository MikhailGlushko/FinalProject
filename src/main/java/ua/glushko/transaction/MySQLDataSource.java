package ua.glushko.transaction;

import org.apache.commons.dbcp.BasicDataSource;
import ua.glushko.configaration.DBConfigurationManager;

import javax.sql.DataSource;

/**
 * MySQL DataSource
 */
public class MySQLDataSource {

    private static final String PROPERTY_NAME_DB_URL = "jdbc:mysql://%s:%s/%s?useSSL=false";
    private static final String PROPERTY_NAME_DB_HOST = "db_host";
    private static final String PROPERTY_NAME_DB_PORT = "db_port";
    private static final String PROPERTY_NAME_DB_NAME = "db_name";
    private static final String PROPERTY_NAME_DB_DRIVER = "db_driver";
    private static final String PROPERTY_NAME_DB_LOGIN = "db_login";
    private static final String PROPERTY_NAME_DB_PASSWORD = "db_password";
    private static final String PROPERTY_NAME_DB_POLL_MAX_ACTIVE = "db_pool_MaxActive";
    private static final String PROPERTY_NAME_DB_POLL_MAX_IDLE = "db_pool_MaxIdle";
    private static final String PROPERTY_NAME_DB_POLL_MAX_WAIT = "db_pool_MaxWait";

    private static final DataSource MYSQL_DATASOURCE = initDataSource();

    public static DataSource getDatasource() {
        return MYSQL_DATASOURCE;
    }

    private static BasicDataSource initDataSource() {
        BasicDataSource dataSource = null;
        String url = String.format(PROPERTY_NAME_DB_URL,
                DBConfigurationManager.getProperty(PROPERTY_NAME_DB_HOST),
                DBConfigurationManager.getProperty(PROPERTY_NAME_DB_PORT),
                DBConfigurationManager.getProperty(PROPERTY_NAME_DB_NAME));

        dataSource = new BasicDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_DRIVER));
        dataSource.setUsername(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_LOGIN));
        dataSource.setPassword(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_PASSWORD));
        dataSource.setMaxActive(Integer.parseInt(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_POLL_MAX_ACTIVE)));
        dataSource.setMaxIdle(Integer.parseInt(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_POLL_MAX_IDLE)));
        dataSource.setMaxWait(Integer.parseInt(DBConfigurationManager.getProperty(PROPERTY_NAME_DB_POLL_MAX_WAIT)));
        return dataSource;
    }

}
