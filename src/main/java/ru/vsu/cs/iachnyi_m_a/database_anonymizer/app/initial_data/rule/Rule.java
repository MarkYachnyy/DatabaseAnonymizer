package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.ColumnGenerator;

public interface Rule {
    ColumnGenerator toGenerator();
    String getTableName();
    String getColumnName();
    float getNullChance();
}
