package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule.distribution.continuous;

import java.util.Random;

public class ExponentialDistribution implements ContinuousDistribution{

    private final float lambda;
    private final Random rand;

    public ExponentialDistribution(float lambda){
        this.lambda = lambda;
        rand = new Random();
    }

    @Override
    public float next() {
        return (float) Math.log(1-rand.nextFloat())/(-lambda);
    }
}
