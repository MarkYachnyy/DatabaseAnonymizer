package ru.vsu.cs.iachnyi_m_a.database_scanner.app.table_mask;

import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.Table;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.DatabaseQueryTool;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.Result;

import javax.sql.DataSource;
import java.util.List;

public class TableMaskBuilder {
    private Table table;
    private DataSource dataSource;

    public TableMaskBuilder(Table table, DataSource dataSource) {
        this.table = table;
        this.dataSource = dataSource;
    }

    public TableMask build(){
        DatabaseQueryTool tool = new DatabaseQueryTool(dataSource);
        List<Result> list = tool.getValues(table.getTableName(), table.getColumns().keySet().toArray(new String[0]));
        System.out.println(list);
        return null;
    }
}
