package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base;

import lombok.Data;

import java.util.List;

@Data
public class Table {
    private String name;
    private List<Column> columns;

    @Override
    public boolean equals(Object obj) {
        return this.name.equals(((Table)obj).name);
    }
}
