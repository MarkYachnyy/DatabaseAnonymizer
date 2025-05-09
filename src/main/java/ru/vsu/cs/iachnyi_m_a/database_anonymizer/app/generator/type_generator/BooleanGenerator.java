package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

public class BooleanGenerator implements ColumnGenerator{

    private final String columnName;
    private final float nullChance;
    private final float trueChance;

    public BooleanGenerator(String columnName, float nullChance, float trueChance) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.trueChance = trueChance;
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
        return new String[]{String.valueOf(Math.random() < trueChance)};
    }
}
