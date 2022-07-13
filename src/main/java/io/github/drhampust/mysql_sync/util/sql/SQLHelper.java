package io.github.drhampust.mysql_sync.util.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.drhampust.mysql_sync.util.objects.SQLColumn;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;

import static io.github.drhampust.mysql_sync.Main.*;

public class SQLHelper {
    private static final String sqlURL;
    private static final HikariConfig config;
    private static final HikariDataSource ds;

    static {
        sqlURL = "jdbc:" + SQL_CONFIG.getSqlType() +
                "://" + SQL_CONFIG.getHost() + ":" + SQL_CONFIG.getPort() +
                "/" + SQL_CONFIG.getDatabase();
        config = new HikariConfig();
        config.setJdbcUrl(sqlURL);
        config.setUsername(SQL_CONFIG.getUsername());
        config.setPassword(SQL_CONFIG.getPassword());

        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void createTable(String tableName, @NotNull List<SQLColumn> columns, String args) {
        // Build string to create table within given database and only if no table with name already exists
        StringBuilder sqlQuery = new StringBuilder(100); // expecting string between 70 - 100 chars
        sqlQuery.append("CREATE TABLE IF NOT EXISTS `").append(SQL_CONFIG.getDatabase()).append("`.`").append(tableName).append("` (");
        for (SQLColumn colum: columns) { // add columns to statement which was received as argument
            sqlQuery.append(colum).append(", ");
        }
        if (args.equals(""))
            sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1); // remove trailing comma and blank space
        sqlQuery.append(args).append(");");


        // Try to perform statement
        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement();) {
            statement.executeUpdate(sqlQuery.toString());
        } catch (SQLException e) { // Statement failed
            e.printStackTrace();
            LOGGER.error("{} Creation of table on SQL Failed!", LOGGER_PREFIX);
        }
    }

    public static void insertNoDuplicates(String db_table, String @NotNull [] columns, Object @NotNull [] values, String sqlKey) {
        // using string builder instead of concatenating strings.
        StringBuilder sqlQuery = new StringBuilder(200); // expecting string between 150 - 200 chars

        // generate first part of sql Query
        sqlQuery.append("INSERT INTO ").append("`").append(SQL_CONFIG.getDatabase()).append("`.`").append(db_table).append("` (");

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

        // Try-with-resource auto closes connection and statements
        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());) {
            int statementIndex = 0; // initialise index to insert into
            // Replace value placeholder '?' with actual values
            for (Object value: values) {
                // Increment statementIndex as first index is 1 and not 0 NOTE: statementIndex++ will increment after insertion
                preparedStatement.setObject(++statementIndex, value);
            }
            // Run SQL Statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) { // Statement failed
            e.printStackTrace();
            LOGGER.error("{} Creation of table on SQL Failed!", LOGGER_PREFIX);
        }
    }

    public static List<Map<String, Object>> simpleSelectWhere(String db_table, String @NotNull [] columns, String keyColumn, Object keyValue) {
        // using string builder instead of concatenating strings.
        StringBuilder sqlQuery = new StringBuilder(200); // expecting string between 150 - 200 chars

        // generate first part of sql Query
        sqlQuery.append("SELECT ");
        if (Arrays.equals(columns, new String[]{}))
            sqlQuery.append("*");
        else {
            for (String colum: columns) {
                sqlQuery.append("`").append(colum).append("`, ");
            }
            // remove trailing comma and blank space
            sqlQuery.deleteCharAt(sqlQuery.length()-1).deleteCharAt(sqlQuery.length()-1);
        }
        sqlQuery.append(" FROM `").append(SQL_CONFIG.getDatabase()).append("`.`").append(db_table).append("` WHERE `").append(keyColumn).append("`=?;");


        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, Object> row;

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery.toString());
             ) {
            preparedStatement.setObject(1, keyValue);

            try(ResultSet rs = preparedStatement.executeQuery();){

                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                while (rs.next()) {
                    row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("{} Creation of table on SQL Failed!", LOGGER_PREFIX);
            throw new RuntimeException(e);
        }
        return resultList;
    }


}
