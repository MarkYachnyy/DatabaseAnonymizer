package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

import java.util.UUID;

public class UUIDPrimaryKeyGenerator implements ColumnGenerator {

    private final String columnName;

    public UUIDPrimaryKeyGenerator(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public float getNullChance() {
        return 0;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{columnName};
    }

    @Override
    public String[] getNextValues() {
        return new String[]{"'" + UUID.randomUUID() + "'"};
    }
}
