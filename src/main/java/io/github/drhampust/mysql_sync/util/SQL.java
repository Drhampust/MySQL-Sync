package io.github.drhampust.mysql_sync.util;


import com.oroarmor.config.ConfigItem;
import com.oroarmor.config.ConfigItemGroup;
import io.github.drhampust.mysql_sync.Main;

import java.sql.*;
import java.util.List;

import static io.github.drhampust.mysql_sync.Main.CONFIG;
import static io.github.drhampust.mysql_sync.Main.LOGGER;

public class SQL {
    //TODO: Add configuration accesses to remove private final static string fields and make SQL useful for more than me
    private static final String host;
    private static final String port;
    private static final String database;
    private static final String username;
    private static final String password;
    private static Connection connection;
    static {
        List<ConfigItem<?>> sqlSection = ((ConfigItemGroup)CONFIG.getConfigs().get(0).getConfigs().get(0)).getConfigs();
        host = (String) sqlSection.get(0).getValue();
        port = (String) sqlSection.get(1).getValue();
        database = (String) sqlSection.get(2).getValue();
        username = (String) sqlSection.get(3).getValue();
        password = (String) sqlSection.get(4).getValue();
    }

    public static void createTable(String tableName, SQLColumn[] columns) {
        // Build string to create table within given database and only if no table with name already exists
        StringBuilder sqlQuery = new StringBuilder(100); // expecting string between 70 - 100 chars
        sqlQuery.append("CREATE TABLE IF NOT EXISTS `").append(database).append("`.`").append(tableName).append("` (");
        for (SQLColumn colum: columns) { // add columns to statement which was received as argument
            sqlQuery.append(colum).append(", ");
        }
        sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1); // remove trailing comma and blank space
        sqlQuery.append(");");


        // initialize statement outside of try scope
        Statement statement = null;

        connectSQL();
        // Try to perform statement
        try {
            statement = connection.createStatement();
            Main.LOGGER.info("Creating SQL table using: {}", sqlQuery.toString());
            statement.executeUpdate(sqlQuery.toString());
        } catch (SQLException e) { // Statement failed
            e.printStackTrace();
            Main.LOGGER.error("Creation of table on SQL Failed!");
        } finally { // Statement succeeded and we need to clean up connections
            try {
                // Close connection
                if (statement != null) {
                    statement.close();
                }
                disconnectSQL();
            } catch (Exception e) {
                Main.LOGGER.error("Clean up of connections Failed!");
                e.printStackTrace();
            }
        }
    }

    public static void insertNoDuplicates(String db_table, String[] columns, Object[] values, String sqlKey) {
        // using string builder instead of concatenating strings.
        StringBuilder sqlQuery = new StringBuilder(200); // expecting string between 150 - 200 chars

        // generate first part of sql Query
        sqlQuery.append("INSERT INTO ").append("`").append(database).append("`.`").append(db_table).append("` (");

        // add columns to statement which was received as argument
        for (String colum: columns) {
            sqlQuery.append("`").append(colum).append("`, ");
        }

        // remove trailing comma and blank space
        sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1);

        // Continue to values part of sql query
        sqlQuery.append(") VALUES (");

        // add prepared statement value holders ('?' char)
        for (String ignored : columns) {
            sqlQuery.append("?").append(", ");
        }

        // remove trailing comma and blank space
        sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1);

        // Continue to update part if row with key already exists in the table
        sqlQuery.append(") ON DUPLICATE KEY UPDATE ");

        // add columns that should be updated (ignoring key colum as we have same the same key as already stored)
        for (String colum : columns) {
            if (colum.equals(sqlKey)) continue; // skip key column
            // Reusing values form earlier VALUES(?, ..., ?) statement
            sqlQuery.append("`").append(colum).append("`=VALUE(").append("`").append(colum).append("`), ");
        }

        // remove trailing comma and blank space
        sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1);

        // end of SQL Query String
        sqlQuery.append(";");


        // create statement outside of try scope, so we can close it after
        PreparedStatement preparedStatement = null;

        // Connect to SQL using config file
        connectSQL();
        try {
            // Convert SQL String into a prepared statement (SQL injection safe)
            preparedStatement = getConnection().prepareStatement(sqlQuery.toString());


            int statementIndex = 0; // initialise index to insert into
            // Replace value placeholder '?' with actual values
            for (Object value: values) {
                // Increment statementIndex as first index is 1 and not 0 NOTE: statementIndex++ will increment after insertion
                preparedStatement.setObject(++statementIndex, value);
            }
            Main.LOGGER.info("Trying to execute following string: {}",preparedStatement.toString());
            // Run SQL Statement
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            disconnectSQL();
            throw new RuntimeException(e);
        } finally { // Statement succeeded and we need to clean up connections
            try {
                // Close connection
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                // disconnect from SQL
                disconnectSQL();
            } catch (Exception e) {
                Main.LOGGER.error("Clean up of connections Failed!");
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
    public static boolean isConnected() {
        if(connection != null) try {
            return connection.isClosed();
        } catch (SQLException ignored) {}
        return false;
    }

    public static boolean connectSQL() {
        if (!isConnected()) {
            try {
                connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":" + port + "/" + database + "?user=" + username  + "&password=" +  password);
            } catch (SQLException e) {
                LOGGER.error("Invalid SQL given in configuration! Mod can not continue to run until correct information is given in config");
                LOGGER.error("Error Message: {}", e.getMessage());
                LOGGER.trace("Stack Trace: {}", (Object) e.getStackTrace()); //TODO: Find a way to make config toggle this
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void disconnectSQL() {
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

}
