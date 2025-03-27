package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.Random;

public class FloatMask implements Mask<Float> {

    private float min;
    private float max;
    private Random random;

    public FloatMask(float min, float max) {
        this.min = min;
        this.max = max;
        random = new Random();
    }

    @Override
    public Float generate() {
        return random.nextFloat(min, max);
    }
}
