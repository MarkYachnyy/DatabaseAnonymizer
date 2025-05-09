package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.continuous.ContinuousDistribution;

public class FloatGenerator implements ColumnGenerator {

    private final String columnName;
    private final float nullChance;
    private final ContinuousDistribution distribution;

    public FloatGenerator(String columnName, float nullChance, ContinuousDistribution distribution) {
        this.columnName = columnName;
        this.distribution = distribution;
        this.nullChance = nullChance;
    }

    @Override
    public float getNullChance() {
        return nullChance;
    }

    @Override
    public String[] getColumnNames() {
        return new String[]{columnName};
    }

    @Override
    public String[] getNextValues() {
        return new String[]{String.valueOf(distribution.next())};
    }
}
