package ru.vsu.cs.iachnyi_m_a.database_scanner.app.table_mask;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.DatabaseValueType;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.Table;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder.*;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.DatabaseQueryTool;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.Result;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableMaskBuilder {
    private Table table;
    private DataSource dataSource;

    public TableMaskBuilder(Table table, DataSource dataSource) {
        this.table = table;
        this.dataSource = dataSource;
    }

    public Map<String, MaskBuilder> build(){
        DatabaseQueryTool tool = new DatabaseQueryTool(dataSource);
        List<Result> list = tool.getValues(table.getTableName(), table.getColumns().keySet().toArray(new String[0]));
        Map<String, MaskBuilder> builders = new HashMap<>();
        for(String columnName: table.getColumns().keySet()){
            builders.put(columnName, createMaskBuilder(table.getColumns().get(columnName)));
        }
        for(Result result: list){
            for(String columnName: table.getColumns().keySet()){
                builders.get(columnName).append(result.getObject(columnName));
            }
        }
        return builders;
    }

    private MaskBuilder createMaskBuilder(DatabaseValueType type){
        return switch (type) {
            case INTEGER -> new IntegerMaskBuilder();
            case BIGINT -> new LongMaskBuilder();
            case UUID -> null;
            case VARCHAR -> new StringMaskBuilder(5);
            case FLOAT -> new FloatMaskBuilder();
            case BOOLEAN -> null;
            case DATE -> new DateMaskBuilder();
        };
    }
}
