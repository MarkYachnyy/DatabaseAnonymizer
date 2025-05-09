package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;

public class LongPrimaryKeyGenerator implements ColumnGenerator {

    private final String columnName;
    private long next = 0L;

    public LongPrimaryKeyGenerator(String columnName) {
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
        next++;
        return new String[]{String.valueOf(next)};
    }
}
