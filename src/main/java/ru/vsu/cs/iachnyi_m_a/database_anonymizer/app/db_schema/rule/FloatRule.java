package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;

import java.util.List;

@Data
public class FloatRule {
    private String tableName;
    private String columnName;

    private ContinuousDistributionType distributionType;
    private float[] params;
    private float nullChance;
}
