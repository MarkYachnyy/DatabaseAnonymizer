package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.ValueType;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key.LongPrimaryKeyGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key.ObjectIDPrimaryKeyGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.type_generator.primary_key.UUIDPrimaryKeyGenerator;

public class PrimaryKeyGeneratorFactory {
    public static ColumnGenerator createColumnGenerator(String columnName, ValueType type){
        ColumnGenerator res = null;
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
