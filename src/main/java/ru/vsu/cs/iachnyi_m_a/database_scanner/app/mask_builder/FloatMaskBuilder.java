package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.FloatMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class FloatMaskBuilder implements MaskBuilder<Float> {

    private float min;
    private float max;

    public FloatMaskBuilder() {
        min=0;
        max=0;
    }

    @Override
    public void append(Float value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    @Override
    public Mask<Float> get() {
        return new FloatMask(min, max);
    }
}
