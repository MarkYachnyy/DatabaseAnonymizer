package ru.vsu.cs.iachnyi_m_a.database_scanner;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.datasource.HikariDataSourceFactory;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.DatabaseSchema;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.DatabaseSchemaScanner;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.db_schema.Table;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.mask_builder.StringMaskBuilder;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.query.DatabaseQueryTool;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.table_mask.TableMask;
import ru.vsu.cs.iachnyi_m_a.database_scanner.app.table_mask.TableMaskBuilder;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DatabaseScannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseScannerApplication.class, args);
        HikariDataSource source = HikariDataSourceFactory.create("pbd_sem5", "postgres", "1234", 20000, 10);
        DatabaseSchemaScanner dbSchemaScanner = new DatabaseSchemaScanner(source);
        DatabaseSchema schema = dbSchemaScanner.scan();
        Table table = schema.getTableByName("users");
        TableMaskBuilder builder = new TableMaskBuilder(table, source);
        builder.build();
//        for (int i = 0; i < 5; i++) {
//            StringBuilder b = new StringBuilder();
//            int min = 'a';
//            int max = 'z' + 1;
//            int l = 1;
//            for (int j = 0; j < l; j++) {
//                b.append((char) (rand.nextInt(min, max)));
//            }
//            System.out.println(b.toString());
//            builder.append(b.toString());
//        }
//        for (int i = 0; i < 5; i++) {
//            StringBuilder b = new StringBuilder();
//            int min = 'a';
//            int max = 'z' + 1;
//            int l = rand.nextInt(2, 10);
//            for (int j = 0; j < l; j++) {
//                b.append((char) (rand.nextInt(min, max)));
//            }
//            System.out.println(b.toString());
//            builder.append(b.toString());
//        }
    }

}
