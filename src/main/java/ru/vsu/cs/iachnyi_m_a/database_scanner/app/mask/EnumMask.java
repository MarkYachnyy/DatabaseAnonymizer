package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

import java.util.List;
import java.util.Random;

public class EnumMask implements Mask<String>{

    private List<String> values;
    private Random random;

    public EnumMask(List<String> values) {
        this.values = values;
        random = new Random();
    }

    @Override
    public String generate() {
        return values.get(random.nextInt(values.size()));
    }
}
