package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public List<List<Table>> getTablesByDependenceLayers() {
        List<List<Table>> res = new ArrayList<>();
        List<Table> tables = new ArrayList<>(getTables());
        List<Table> toRemove = new ArrayList<>();
        res.add(new ArrayList<>());

        for (Table table : tables) {
            if (table.getForeignKeys().isEmpty()) {
                res.getFirst().add(table);
                toRemove.add(table);
            }
        }

        tables.removeAll(toRemove);
        int i = 1;
        while (!tables.isEmpty()) {
            res.add(new ArrayList<>());
            toRemove.clear();
            for (Table table : tables) {
                boolean table_belongs = true;
                for (Map.Entry<String, Column> entry : table.getForeignKeys().entrySet()) {
                    boolean fk_belongs = false;
                    for (int j = 0; j < i; j++) {
                        if (listContainsTable(res.get(j), entry.getValue().getTableName())) {
                            fk_belongs = true;
                            break;
                        }
                    }
                    if (!fk_belongs) {
                        table_belongs = false;
                        break;
                    }
                }
                if (table_belongs) {
                    res.get(i).add(table);
                    toRemove.add(table);
                }

            }
            tables.removeAll(toRemove);
            i++;
        }
        return res;
    }

    private boolean listContainsTable(List<Table> tables, String tableName) {
        return tables.stream().anyMatch(t -> t.getTableName().equals(tableName));
    }
}
