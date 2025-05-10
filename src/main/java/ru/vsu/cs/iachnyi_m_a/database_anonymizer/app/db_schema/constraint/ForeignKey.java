package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.IntegerRule;

@Data
public class ForeignKey {
    private String sourceTableName;
    private String sourceColumnName;

    private String targetTableName;
    private String targetColumnName;

    private int sourceMinCount;
    private int sourceMaxCount;
    private float sourceZeroChance;
    private float targetZeroChance;
}
