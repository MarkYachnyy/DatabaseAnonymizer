package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.constraint;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule.IntegerRule;

@Data
public class ForeignKey {
    private String sourceTableName;
    private String sourceColumnName;

    private String targetTableName;
    private String targetColumnName;

    private IntegerRule sourceCountRule;
    private float sourceZeroChance;
    private float rightZeroChance;
}
