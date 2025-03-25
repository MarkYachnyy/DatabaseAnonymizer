package ru.vsu.cs.iachnyi_m_a.database_scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.HikariDataSourceFactory;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.DatabaseSchemaScanner;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.Table;

import java.util.List;

@SpringBootApplication
public class DatabaseScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseScannerApplication.class, args);
        DatabaseSchemaScanner scanner = new DatabaseSchemaScanner(HikariDataSourceFactory.create("pbd_sem5", "postgres", "1234", 20000, 10));
        List<Table> tables = scanner.scan().getTables();
        for(Table table : tables) {
            System.out.println(table.toString());
        }
    }

}
