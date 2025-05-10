package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator;

public interface PrimaryKeyGenerator {
    String nextValue();
    String getColumnName();
}
