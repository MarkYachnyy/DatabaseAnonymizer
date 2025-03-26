package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DatabaseSchema {

    private List<Table> tables;

    public DatabaseSchema() {
        this.tables = new ArrayList<>();
    }

    public Table getTableByName(String tableName) {
        return tables.stream().filter(t -> t.getTableName().equals(tableName)).findFirst().orElse(null);
    }
}
