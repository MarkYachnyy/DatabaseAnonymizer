package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.Random;

public class FloatMask implements Mask<Float> {

    private float min;
    private float max;
    private Random random;

    @Override
    public Float generate() {
        return random.nextFloat(min, max);
    }
}
