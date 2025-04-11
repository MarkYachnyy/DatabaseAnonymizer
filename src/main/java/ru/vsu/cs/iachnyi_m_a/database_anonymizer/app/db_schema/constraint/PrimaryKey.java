package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint;

import lombok.Data;

@Data
public class PrimaryKey {
    private String tableName;
    private String columnName;
}
