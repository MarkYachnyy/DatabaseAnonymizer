package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateGenerator extends ColumnGenerator{
    private final String columnName;
    private final float nullChance;
    private final Long minDate;
    private final Long maxDate;
    private final Random rand = new Random();
    private final boolean unique;
    private final List<Set<String>> alreadyGeneratedValues;



    public DateGenerator(String columnName, float nullChance, Long minDate, Long maxDate, boolean unique) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.unique = unique;
        this.alreadyGeneratedValues = List.of(new HashSet<>());

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
    protected String[] generateValues() {
        long dateLong = rand.nextLong(minDate, maxDate);
        Date date = new Date(dateLong);
        return new String[]{"'" + new SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.ROOT
        ).format(date) + "'"};
    }

    @Override
    public boolean isUnique(){
        return unique;
    }

    @Override
    public List<Set<String>> getAlreadyGeneratedValues() {
        return alreadyGeneratedValues;
    }
}
