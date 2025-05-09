package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.interval;

import lombok.Data;

import java.util.Map;

@Data
public class IntervalRule {

    private String tableName;
    private String columnName;

    private float nullChance;
    private Map<Interval, AllenAlgebraRelation> relations;
}
