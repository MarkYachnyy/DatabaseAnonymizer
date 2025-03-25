package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask;

public class LongMask implements Mask<Long>{

    @Override
    public Long generate() {
        return 0L;
    }
}
