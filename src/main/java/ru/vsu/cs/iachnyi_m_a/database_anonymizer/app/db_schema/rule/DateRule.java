package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;

@Data
public class DateRule {
    private String tableName;
    private String columnName;

    private String startDate;
    private String endDate;

    private float nullChance;
}
