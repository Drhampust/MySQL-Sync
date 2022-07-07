package net.fabricmc.example;


import java.sql.*;

public class sql {
    private static final String host = "127.0.0.1";
    private static final String port = "3306";
    private static final String database = "Minecraft";
    private static final String username = "REDACTED";
    private static final String password = "REDACTED";
    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public boolean connectSQL() {
        if (!isConnected()) {
            try {
                ExampleMod.LOGGER.info("Trying to connect to SQL...");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            } catch (SQLException e) {
                ExampleMod.LOGGER.error("Connection to SQL Failed!");
                e.printStackTrace();
                return false;
            }
        }
        ExampleMod.LOGGER.info("Connected to SQL");
        return true;
    }

    public void disconnectSQL() {
        if (isConnected()) { // Only disconnect if we are connected
            ExampleMod.LOGGER.info("Disconnecting from SQL...");
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                ExampleMod.LOGGER.error("Failed to disconnect from SQL!");
            } finally {
                ExampleMod.LOGGER.info("Disconnected from SQL");
            }
        }
    }

    public void createTable(String tableName, String tableContent) {
        ExampleMod.LOGGER.info("Trying to create a table on SQL...");
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
            ExampleMod.LOGGER.error("Creation of table on SQL Failed!");
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
                ExampleMod.LOGGER.error("Clean up off connections Failed!");
                e.printStackTrace();
            }
        }
        ExampleMod.LOGGER.info("Table created successfully");
    }

    public void insertToTable() {

    }


    public void loadFromTable() {

    }

    public Connection getConnection() {
        return connection;
    }
}
