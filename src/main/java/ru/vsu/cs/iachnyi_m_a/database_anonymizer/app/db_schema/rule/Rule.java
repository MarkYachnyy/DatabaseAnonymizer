package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

public interface Rule {
    ColumnGenerator toGenerator();
    String getTableName();
    String getColumnName();
    float getNullChance();
}
