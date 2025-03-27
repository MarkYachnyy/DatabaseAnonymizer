package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class IntegerMaskBuilder implements MaskBuilder<Integer> {

    private int min;
    private int max;

    public IntegerMaskBuilder(){
        min = 0;
        max = 0;
    }

    @Override
    public void append(Integer value) {
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    @Override
    public Mask<Integer> get() {
        return new IntegerMask(min, max);
    }
}
