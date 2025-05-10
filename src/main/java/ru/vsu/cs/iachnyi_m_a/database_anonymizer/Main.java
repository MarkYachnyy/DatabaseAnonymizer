package ru.vsu.cs.iachnyi_m_a.database_anonymizer;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.datasource.HikariDataSourceFactory;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.DatabaseSchema;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.base.Table;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.constraint.ConstraintSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.db_schema.rule.RuleSet;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.DatabaseGenerator;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.generator.query.QueryTool;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws SQLException, IOException {
        HikariDataSource source = HikariDataSourceFactory.create("anonymizer", "postgres", "1234", 20000, 10);
        DatabaseGenerator generator = new DatabaseGenerator(new QueryTool(source));
        generator.fillDatabase(
                new Gson().fromJson(readFileToString("./schema.json"), DatabaseSchema.class),
                new Gson().fromJson(readFileToString("./constraint.json"), ConstraintSet.class),
                new Gson().fromJson(readFileToString("./rules.json"), RuleSet.class));
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 2, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
//        executor.allowCoreThreadTimeOut(true);
//        for (int i = 0; i < 10; i++) {
//            executor.submit(() -> {
//                for (int i1 = 0; i1 < 10; i1++) {
//                    System.out.println(i1);
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });
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
