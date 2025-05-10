package ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryExecutor {
    private Connection conn;

    public QueryExecutor(Connection conn) {
        this.conn = conn;
    }

    public void executeQuery(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeQuery(query);
    }
}
