package io.github.drhampust.mysql_sync.util.config;

import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.ConfigOptions;

public class SQLConfig extends Config {

    @ConfigEntry(requiresRestart = true, comment = "\nSpecifies what host to try and connect to (Default: 127.0.0.1 which is the same as using localhost)")
    public String host = "127.0.0.1";

    @ConfigEntry(requiresRestart = true, comment = "\nSpecifies what port the SQL database is running on (Default: 3306)")
    public int port = 3306;

    @ConfigEntry(requiresRestart = true, comment = "\nEnter the name of the database on the SQL server")
    public String database = "";

    @ConfigEntry(requiresRestart = true, comment = "\nEnter username used to login to SQL with access to given database")
    public String username = "";

    @ConfigEntry(requiresRestart = true, comment = "\nEnter password required to login with given username")
    public String password = "";

    @ConfigEntry.Dropdown
    @ConfigEntry(requiresRestart = true, comment = "\nEnter what database you are using\n Available options:\nMySQL\nPostgreSQL\nMicrosoftSQL\nMariaDB (Default)")
    public SQLType sqlType = SQLType.MariaDB;


    public enum SQLType {
        MySQL("mysql"),
        PostgreSQL("postgresql"),
        MicrosoftSQL("sqlserver"),
        MariaDB("mariadb");

        private final String subProtocol;
        SQLType(String subProtocol) {
            this.subProtocol = subProtocol;
        }
        public String getSubProtocol() {
            return subProtocol;
        }
    }


    public SQLConfig() {
        super(ConfigOptions.mod("mysql_sync").branch(new String[]{"", "SQL"}).fileHeader("Main Configuration file for SQL Options"));
    }

}
