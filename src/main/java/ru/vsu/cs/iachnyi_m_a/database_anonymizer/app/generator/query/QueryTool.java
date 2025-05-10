package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryTool {
    private DataSource dataSource;

    public QueryTool(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public QueryExecutor getQueryExecutor() throws SQLException {
        return new QueryExecutor(dataSource.getConnection());
    }
}
