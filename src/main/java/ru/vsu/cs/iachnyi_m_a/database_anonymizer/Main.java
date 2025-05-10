package ru.vsu.cs.iachnyi_m_a.database_anonymizer;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.datasource.HikariDataSourceFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.DatabaseGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query.QueryTool;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


public class Main {

    public static void main(String[] args) throws SQLException {
        HikariDataSource source = HikariDataSourceFactory.create("anonymizer", "postgres", "1234", 20000, 10);
        DatabaseGenerator generator = new DatabaseGenerator(new QueryTool(source));
        generator.fillDatabase(Arrays.stream(new Gson().fromJson("""
                [
                {
                    "name":"table1",
                    "columns":[
                        {
                            "name":"id",
                            "type":"UUID"
                        },
                        {
                            "name":"name",
                            "type":"VARCHAR"
                        },
                        {
                            "name":"age",
                            "type":"INTEGER"
                        },
                        {
                            "name":"mark",
                            "type":"FLOAT"
                        },
                        {
                            "name":"tralalelo",
                            "type": INT_INTERVAL
                        }
                    ]
                }
                ]
                """, Table[].class)).toList(), null, null);
    }

}
