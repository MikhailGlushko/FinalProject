package ua.glushko.transaction;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * H2 DataSource
 */
public class H2DataSource {
    private static final String URL = "jdbc:h2:mem:REPAIR_AGENCY;" +
            "MODE=MySQL;"
            + "INIT=CREATE SCHEMA IF NOT EXISTS REPAIR_AGENCY\\; "
            + "SET SCHEMA REPAIR_AGENCY\\; "
            + "RUNSCRIPT FROM 'classpath:/scripts/repair_agency_dump_h2.sql'";

    public static JdbcConnectionPool getInstance(){
        return JdbcConnectionPool.create(URL, "", "");
    }
}
