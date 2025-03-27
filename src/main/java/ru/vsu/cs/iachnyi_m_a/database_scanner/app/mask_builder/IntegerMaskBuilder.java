package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.IntegerMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public class IntegerMaskBuilder implements MaskBuilder{

    private int min;
    private int max;

    public IntegerMaskBuilder(){
        min = 0;
        max = 0;
    }

    @Override
    public void append(Object value) {
        if(!(value instanceof Integer)){
            throw new IllegalArgumentException();
        }
        int intValue = (Integer)value;
        min = Math.min(min, intValue);
        max = Math.max(max, intValue);
    }

    @Override
    public Mask get() {
        return new IntegerMask(min, max);
    }
}
