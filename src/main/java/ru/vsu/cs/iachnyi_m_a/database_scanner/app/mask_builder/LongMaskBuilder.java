package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.LongMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class LongMaskBuilder implements MaskBuilder<Long>{

    private long min;
    private long max;

    public LongMaskBuilder() {
        min = 0;
        max = 0;
    }

    @Override
    public void append(Long value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    @Override
    public Mask<Long> get() {
        return new LongMask(min, max);
    }
}
