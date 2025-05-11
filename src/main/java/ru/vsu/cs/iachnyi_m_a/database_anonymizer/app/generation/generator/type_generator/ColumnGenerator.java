package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator;

public interface ColumnGenerator {
    float getNullChance();
    String[] getColumnNames();
    String[] getNextValues();
}
