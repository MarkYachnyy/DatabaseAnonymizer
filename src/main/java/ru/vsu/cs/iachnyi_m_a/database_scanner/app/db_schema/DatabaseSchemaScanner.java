package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.Result;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseSchemaScanner {

    private final DataSource dataSource;
    private List<Table> tables;

    public DatabaseSchemaScanner(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DatabaseSchema scan() {
        DatabaseSchema databaseSchema = new DatabaseSchema();
        for (String tableName : scanAllTableNames()) {
            Table table = new Table(tableName, scanColumns(tableName));
            databaseSchema.getTables().add(table);
        }
        Map<String, List<String>> primaryKeys = scanAllPrimaryKeys();
        for (String tableName : primaryKeys.keySet()) {
            databaseSchema.getTableByName(tableName).getPrimaryKeys().addAll(primaryKeys.get(tableName));
        }
        return databaseSchema;
    }

    private List<String> scanAllTableNames() {
        List<String> tableNames = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema = 'public';");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return tableNames;
    }


    private DatabaseValueType getType(String s) {
        return switch (s.toLowerCase()) {
            case "bigint" -> DatabaseValueType.BIGINT;
            case "character varying" -> DatabaseValueType.VARCHAR;
            case "uuid" -> DatabaseValueType.UUID;
            case "double precision" -> DatabaseValueType.FLOAT;
            case "integer" -> DatabaseValueType.INTEGER;
            case "boolean" -> DatabaseValueType.BOOLEAN;
            default -> null;
        };
    }

    private Map<String, List<String>> scanAllPrimaryKeys() {
        Map<String, List<String>> primaryKeys = new HashMap<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("""
                                        SELECT kcy.column_name AS column_name, kcy.table_name AS table_name FROM information_schema.key_column_usage AS kcy JOIN information_schema.table_constraints AS tc\s
                    ON tc.constraint_name=kcy.constraint_name JOIN information_schema.tables AS tt ON kcy.table_name=tt.table_name
                    WHERE tt.table_schema = 'public' AND tc.constraint_type='PRIMARY KEY';
                    """);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String tableName = resultSet.getString("table_name");
                if(!primaryKeys.containsKey(tableName)) primaryKeys.put(tableName, new ArrayList<>());
                primaryKeys.get(tableName).add(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return primaryKeys;
    }

    private Map<String, DatabaseValueType> scanColumns(String tableName) {
        Map<String, DatabaseValueType> columns = new HashMap<>();
        try (Connection con = dataSource.getConnection()) {
            PreparedStatement statement = con.prepareStatement("SELECT * FROM information_schema.columns WHERE table_name = ?;");
            statement.setString(1, tableName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                columns.put(resultSet.getString("column_name"), getType(resultSet.getString("data_type")));
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return columns;
    }
}
