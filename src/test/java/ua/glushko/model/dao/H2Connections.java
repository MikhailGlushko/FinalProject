package ua.glushko.model.dao;

import org.h2.jdbcx.JdbcConnectionPool;

class H2Connections {
    private static final String URL = "jdbc:h2:mem:REPAIR_AGENCY;"+
            "MODE=MySQL;"
            +"INIT=CREATE SCHEMA IF NOT EXISTS REPAIR_AGENCY\\; "
            +"SET SCHEMA REPAIR_AGENCY\\; "
            +"RUNSCRIPT FROM 'classpath:/scripts/repair_agency_dump_h2.sql'";

    public static final JdbcConnectionPool H2_CONNECTION_POOL = JdbcConnectionPool.create(URL,"","");
}
