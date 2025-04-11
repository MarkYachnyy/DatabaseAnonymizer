package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;

import java.util.Map;

@Data
public class IntervalRule {
    private Map<Interval, AllenAlgebraRelation> relations;
}
