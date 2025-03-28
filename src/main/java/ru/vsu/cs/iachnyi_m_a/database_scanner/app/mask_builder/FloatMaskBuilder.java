package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.FloatMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class FloatMaskBuilder implements MaskBuilder {

    private float min;
    private float max;

    public FloatMaskBuilder() {
        min=0;
        max=0;
    }

    @Override
    public void append(Object value) {
        if(!(value instanceof Float)) throw new IllegalArgumentException();
        float f = (Float)value;
        min = Math.min(min, f);
        max = Math.max(max, f);
    }

    @Override
    public Mask get() {
        return new FloatMask(min, max);
    }
}
