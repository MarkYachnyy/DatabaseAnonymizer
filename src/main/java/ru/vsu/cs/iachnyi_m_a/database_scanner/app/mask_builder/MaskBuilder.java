package ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask.Mask;

public interface MaskBuilder<T>{
    void append(T value);
    Mask<T> get();
}
