package io.github.drhampust.mysql_sync.util;


import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import io.github.drhampust.mysql_sync.Main;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.sql.*;
import java.util.Base64;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;
import static org.spongepowered.include.com.google.common.collect.ImmutableList.of;

public class SQL {
    //TODO: Add configuration accesses to remove private final static string fields and make SQL useful for more than me
    private static final String host;
    private static final String port;
    private static final String database;
    private static final String username;
    private static final String password;
    private Connection connection;
    static {
        List<ConfigItem<?>> sqlSection = ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs();
        host = (String) sqlSection.get(0).getValue();
        port = (String) sqlSection.get(1).getValue();
        database = (String) sqlSection.get(2).getValue();
        username = (String) sqlSection.get(3).getValue();
        password = (String) sqlSection.get(4).getValue();
    }

    public boolean isConnected() {
        if(connection != null) try {
            return connection.isClosed();
        } catch (SQLException ignored) {}
        return false;
    }

    public boolean connectSQL() {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                LOGGER.error("Invalid SQL given in configuration! Mod can not continue to run until correct information is given in config");
                LOGGER.error("Error Message: {}", e.getMessage());
                LOGGER.trace("Stack Trace: {}", (Object) e.getStackTrace()); //TODO: Find a way to make config toggle this
                return false;
            }
        }
        return true;
    }

    public void disconnectSQL() {
        if (isConnected()) { // Only disconnect if we are connected
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                Main.LOGGER.error("Failed to disconnect from SQL!");
            } finally {
            }
        }
    }

    public void createTable(String tableName, String tableContent) {
        // Build string to create table within given database and only if no table with name already exists
        String table = "CREATE TABLE IF NOT EXISTS " + database + "." + tableName + "(" + tableContent + ")";
        // initialize statement outside of try scope
        Statement statement = null;

        // if not connected to SQL connect
        if (!isConnected()) {
            connectSQL();
        }

        // Try to perform statement
        try {
            statement = connection.createStatement();
            statement.executeUpdate(table);
        } catch (SQLException e) { // Statement failed
            e.printStackTrace();
            Main.LOGGER.error("Creation of table on SQL Failed!");
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
