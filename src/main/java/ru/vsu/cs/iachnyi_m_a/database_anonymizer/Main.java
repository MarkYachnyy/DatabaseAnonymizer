package ru.vsu.cs.iachnyi_m_a.database_anonymizer;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.datasource.HikariDataSourceFactory;

import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.type_generator.ColumnGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.generator.DatabaseGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generation.query.QueryTool;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.AllenAlgebraRelation;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.date_interval.DateIntervalRelation;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.rule.interval.date_interval.DateIntervalRule;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.initial_data.schema.DatabaseSchema;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        HikariDataSource source = HikariDataSourceFactory.create("anonymizer", "postgres", "1234", 1000, 10);
        DatabaseGenerator generator = new DatabaseGenerator(new QueryTool(source));
        generator.fillDatabase(
                new Gson().fromJson(readFileToString("./test_data/schema.json"), DatabaseSchema.class),
                new Gson().fromJson(readFileToString("./test_data/constraint.json"), ConstraintSet.class),
                new Gson().fromJson(readFileToString("./test_data/rules.json"), RuleSet.class),
                "users",
                150);

        DateIntervalRule rule = new DateIntervalRule();
        rule.setRelations(List.of(new DateIntervalRelation("2022-01-01", "2022-02-27", AllenAlgebraRelation.D),new DateIntervalRelation("2022-02-10", "2022-02-20", AllenAlgebraRelation.O)));
        ColumnGenerator generator1 = rule.toGenerator(true);
//        for (int i = 0; i < 50; i++) {
//            System.out.println(Arrays.toString(generator1.getNextValues()));
//        }
    }

    public static String readFileToString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

}
