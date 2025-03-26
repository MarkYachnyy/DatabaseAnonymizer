package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.Random;

public class LongMask implements Mask<Long>{

    private long min;
    private long max;
    private Random random;

    public LongMask(long min, long max){
        this.min = min;
        this.max = max;
        random = new Random();
    }
    @Override
    public Long generate() {
        return random.nextLong(min, max);
    }
}
