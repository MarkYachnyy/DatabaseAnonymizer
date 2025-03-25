package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSchema {
    private List<Table> tables = new ArrayList<Table>();

    public DatabaseSchema() {
        this.tables = new ArrayList<>();
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Table getTableByName(String tableName) {
        return tables.stream().filter(t -> t.getTableName().equals(tableName)).findFirst().orElse(null);
    }
}
