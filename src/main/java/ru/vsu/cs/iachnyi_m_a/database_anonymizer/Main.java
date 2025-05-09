package ru.vsu.cs.iachnyi_m_a.database_anonymizer;

import com.zaxxer.hikari.HikariDataSource;
import ru.vsu.cs.iachnyi_m_a.database_anonymizer.app.datasource.HikariDataSourceFactory;


public class Main {

    public static void main(String[] args) {
        HikariDataSource source = HikariDataSourceFactory.create("pbd_sem5", "postgres", "1234", 20000, 10);
    }

}
