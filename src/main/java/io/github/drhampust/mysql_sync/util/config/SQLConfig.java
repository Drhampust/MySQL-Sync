package io.github.drhampust.mysql_sync.util.config;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

public class SQLConfig extends Config {

    @ConfigEntry(requiresRestart = true, comment = "\nSpecifies what host to try and connect to (Default: 127.0.0.1 which is the same as using localhost)")
    private final String host = "127.0.0.1";

    @ConfigEntry(requiresRestart = true, comment = "\nSpecifies what port the SQL database is running on (Default: 3306)")
    private final int port = 3306;

    @ConfigEntry(requiresRestart = true, comment = "\nEnter the name of the database on the SQL server")
    private final String database = "";

    @ConfigEntry(requiresRestart = true, comment = "\nEnter username used to login to SQL with access to given database")
    private final String username = "";

    @ConfigEntry(requiresRestart = true, comment = "\nEnter password required to login with given username")
    private final String password = "";


    @ConfigEntry.Dropdown
    @ConfigEntry(requiresRestart = true, comment = "\nEnter what database you are using\n Available options:\nMySQL\nPostgreSQL\nMicrosoftSQL\nMariaDB (Default)")
    private final SQLType sqlType = SQLType.MariaDB;


    @SuppressWarnings("unused")
    public enum SQLType {
        MySQL("mysql"),
        PostgreSQL("postgresql"),
        MicrosoftSQL("sqlserver"),
        MariaDB("mariadb");

        private final String subProtocol;
        SQLType(String subProtocol) {
            this.subProtocol = subProtocol;
        }
        private String getSubProtocol() {
            return subProtocol;
        }
    }

    public SQLConfig() {
        super(ConfigOptions.mod("mysql_sync").branch(new String[]{"", "SQL"}).fileHeader("Main Configuration file for SQL Options"));
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getSqlType() {
        return sqlType.getSubProtocol();
    }

}
