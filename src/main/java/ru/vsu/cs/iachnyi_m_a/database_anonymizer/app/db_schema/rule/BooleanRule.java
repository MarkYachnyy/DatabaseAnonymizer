package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;

@Data
public class BooleanRule {
    private String tableName;
    private String columnName;
    private float trueChance;
    private float nullChance;
}
