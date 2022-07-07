package io.github.drhampust.mysql_sync;


import com.oroarmor.config.ConfigItemGroup;

import java.sql.*;
import java.util.Base64;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;

public class SQL {
    //TODO: Add configuration accesses to remove private final static string fields and make SQL useful for more than me
    private static final String host;
    private static final String port;
    private static final String database;
    private static final String username;
    private static final String password;
    private Connection connection;
    static {
        host = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(0).getValue();
        port = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(1).getValue();
        database = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(2).getValue();
        username = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(3).getValue();
        password = (String) ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs().get(4).getValue();
    }

    public boolean isConnected() {
        return (connection != null);
    }

    public boolean connectSQL() {
        if (!isConnected()) {
            try {
                Main.LOGGER.info("Trying to connect to SQL...");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                LOGGER.error("Invalid SQL given in configuration! Mod can not continue to run until correct information is given in config");
                LOGGER.error("Error Message: {}", e.getMessage());
                //if(LOGGER.isDebugEnabled()) e.printStackTrace(); //TODO: Find a way to make config toggle this
                return false;
            }
        }
        Main.LOGGER.info("Connected to SQL");
        return true;
    }

    public void disconnectSQL() {
        if (isConnected()) { // Only disconnect if we are connected
            Main.LOGGER.info("Disconnecting from SQL...");
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Main.LOGGER.error("Failed to disconnect from SQL!");
            } finally {
                Main.LOGGER.info("Disconnected from SQL");
            }
        }
    }

    public void createTable(String tableName, String tableContent) {
        Main.LOGGER.info("Trying to create a table on SQL...");
        // Build string to create table within given database and only if no table with name already exists
        String table = "CREATE TABLE IF NOT EXISTS " + database + "." + tableName + "(" + tableContent + ")";
        // initialize statement outside of try scope
        Statement statement = null;

        // if not connected to SQL connect
        if (!isConnected()) {
            if (!connectSQL()) return;
        }

        // Try to perform statement
        try {
            statement = connection.createStatement();
            statement.executeUpdate(table);
        } catch (SQLException e) { // Statement failed
            e.printStackTrace();
            Main.LOGGER.error("Creation of table on SQL Failed!");
            return;
        } finally { // Statement succeeded and we need to clean up connections
            try {
                // Close connection
                if (statement != null) {
                    statement.close();
                }
                // disconnect from SQL
                disconnectSQL();
            } catch (Exception e) {
                Main.LOGGER.error("Clean up off connections Failed!");
                e.printStackTrace();
            }
        }
        Main.LOGGER.info("Table created successfully");
    }

    public void insertToTable(Base64 data) {

    }


    public Base64 loadFromTable() {
        return null;
    }

    public Connection getConnection() {
        return connection;
    }
}
