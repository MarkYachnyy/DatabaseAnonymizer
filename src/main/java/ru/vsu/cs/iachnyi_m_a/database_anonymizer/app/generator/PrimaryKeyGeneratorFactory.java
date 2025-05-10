package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator.PrimaryKeyGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator.LongPrimaryKeyGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator.ObjectIDPrimaryKeyGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.primary_key_generator.UUIDPrimaryKeyGenerator;

public class PrimaryKeyGeneratorFactory {
    public static PrimaryKeyGenerator createColumnGenerator(String columnName, ValueType type){
        PrimaryKeyGenerator res = null;
        switch (type) {
            case BIGINT : {
                res = new LongPrimaryKeyGenerator(columnName);
                break;
            }
            case UUID : {
                res = new UUIDPrimaryKeyGenerator(columnName);
                break;
            }
            case OBJECT_ID: {
                res = new ObjectIDPrimaryKeyGenerator(columnName);
                break;
            }
        }
        return res;
    }
}
