package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule.distribution.continuous;

import java.util.Random;

public class NormalDistribution implements ContinuousDistribution{

    private final float mean;
    private final float stdDeviation;
    private final Random rand;

    public NormalDistribution(float mean, float stdDeviation){
        this.mean = mean;
        this.stdDeviation = stdDeviation;
        rand = new Random();
    }

    @Override
    public float next() {
        return (float) rand.nextGaussian(mean, stdDeviation);
    }
}
