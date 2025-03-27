package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.LongMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class LongMaskBuilder implements MaskBuilder{

    private long min;
    private long max;

    public LongMaskBuilder() {
        min = 0;
        max = 0;
    }

    @Override
    public void append(Object value) {
        if(!(value instanceof Long)){
            throw new IllegalArgumentException();
        }
        long longValue = (Long)value;
        min = Math.min(min, longValue);
        max = Math.max(max, longValue);
    }

    @Override
    public Mask get() {
        return new LongMask(min, max);
    }
}
