package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.distribution.discrete.DiscreteDistribution;

public class IntegerGenerator implements ColumnGenerator {

    private final String columnName;
    private final float nullChance;
    private final DiscreteDistribution distribution;

    public IntegerGenerator(String columnName, float nullChance, DiscreteDistribution distribution) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.distribution = distribution;
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
