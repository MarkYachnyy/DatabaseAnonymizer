package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

import java.util.UUID;

public class UUIDPrimaryKeyGenerator implements PrimaryKeyGenerator  {

    private final String columnName;

    public UUIDPrimaryKeyGenerator(String columnName) {
        this.columnName = columnName;
    }


    @Override
    public String nextValue() {
        return "'" + UUID.randomUUID() + "'";
    }

    @Override
    public String getColumnName() {
        return columnName;
    }
}
