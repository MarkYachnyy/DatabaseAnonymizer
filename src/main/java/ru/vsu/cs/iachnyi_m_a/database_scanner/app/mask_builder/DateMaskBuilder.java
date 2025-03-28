package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import lombok.ToString;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

import java.sql.Date;
import java.util.Calendar;

@ToString
public class DateMaskBuilder implements MaskBuilder{

    private int minDay;
    private int maxDay;
    private int minMonth;
    private int maxMonth;
    private int minYear;
    private int maxYear;

    public DateMaskBuilder() {
        minDay = 30;
        maxDay = 1;
        minMonth = 12;
        maxMonth = 1;
        minYear = 3000;
        maxYear = 1;
    }

    @Override
    public void append(Object value) {
        if(!(value instanceof Date date)) throw new IllegalArgumentException();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        minDay = Math.min(minDay, day);
        maxDay = Math.max(maxDay, day);
        minMonth = Math.min(minMonth, month);
        maxMonth = Math.max(maxMonth, month);
        minYear = Math.min(minYear, year);
        maxYear = Math.max(maxYear, year);
    }

    @Override
    public Mask get() {
        return null;
    }
}
