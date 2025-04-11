package ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.rule.distribution.discrete;

import java.util.Random;

public class PoissonDistribution implements DiscreteDistribution {

    private float mean;
    private Random rand;

    public PoissonDistribution(float mean) {
        this.mean = mean;
        rand = new Random();
    }

    @Override
    public int next() {
        int n = 0;
        double p = 1;
        while (p > Math.exp(-mean)) {
            double u = rand.nextDouble();
            n++;
            p *= u;
        }
        return n;
    }
}
