package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class Column {
    String tableName;
    String columnName;
}
