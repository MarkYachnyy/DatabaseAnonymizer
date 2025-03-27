package ru.vsu.cs.iachnyi_m_a.database_scanner;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.datasource.HikariDataSourceFactory;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.DatabaseSchemaScanner;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.Table;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.DatabaseQueryTool;

import java.util.List;

@SpringBootApplication
public class DatabaseScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseScannerApplication.class, args);
        HikariDataSource source = HikariDataSourceFactory.create("pbd_sem5", "postgres", "1234", 20000, 10);
        DatabaseQueryTool tool = new DatabaseQueryTool(source);
        System.out.println(tool.getValues("users", new String[]{"id", "password_hash"}));
    }

}
