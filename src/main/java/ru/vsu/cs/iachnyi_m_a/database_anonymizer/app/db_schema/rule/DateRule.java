package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule;

import lombok.Data;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.DateGenerator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Data
public class DateRule implements Rule {
    private String tableName;
    private String columnName;

    private String startDate;
    private String endDate;

    private float nullChance;

    @Override
    public ColumnGenerator toGenerator() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1;
        LocalDate date2;
        date1 = LocalDate.parse(startDate, formatter);
        date2 = LocalDate.parse(endDate, formatter);

        return new DateGenerator(
                columnName,
                nullChance,
                date1.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
                date2.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
    }
}
