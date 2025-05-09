package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class DateGenerator implements ColumnGenerator{
    private final String columnName;
    private final float nullChance;
    private final Long minDate;
    private final Long maxDate;
    private final Random rand = new Random();

    public DateGenerator(String columnName, float nullChance, Long minDate, Long maxDate) {
        this.columnName = columnName;
        this.nullChance = nullChance;
        this.minDate = minDate;
        this.maxDate = maxDate;
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
        long dateLong = rand.nextLong(minDate, maxDate);
        Date date = new Date(dateLong);
        return new String[]{new SimpleDateFormat(
                "dd-MM-yyyy",
                Locale.ROOT
        ).format(date)};
    }
}
