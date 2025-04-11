package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.base;

import lombok.Data;

@Data
public class Column {
    String name;
    ValueType type;
    boolean notNull;
}
