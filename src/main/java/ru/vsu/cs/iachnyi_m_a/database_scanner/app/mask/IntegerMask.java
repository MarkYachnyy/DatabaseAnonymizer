package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.Random;

public class IntegerMask implements Mask{

    private int min;
    private int max;
    private Random random;

    public IntegerMask(int min, int max){
        this.min = min;
        this.max = max;
        random = new Random();
    }

    @Override
    public Integer generate() {
        return random.nextInt(min, max);
    }
}
