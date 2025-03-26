package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Table {
    private String tableName;
    private Map<String, DatabaseValueType> columns;
    private List<String> primaryKeys;
    private Map<String, Column> foreignKeys;

    public Table(String name, Map<String, DatabaseValueType> columns) {
        this.tableName = name;
        this.columns = columns;
        this.primaryKeys = new ArrayList<>();
        this.foreignKeys = new HashMap<>();
    }


}
