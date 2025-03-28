package ru.vsu.cs.iachnyi_m_a.database_scanner.app.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseQueryTool {

    private DataSource dataSource;

    public DatabaseQueryTool(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Result> getValuesPage(String tableName, String[] columnNames, String orderBy, long from, long to){
        List<Result> res = new ArrayList<>();
        try(Connection con = dataSource.getConnection()){
            PreparedStatement statement = con.prepareStatement(String.format("SELECT %s FROM %s ORDER BY %s LIMIT ? OFFSET ?", String.join(", ", columnNames), tableName, orderBy));
            statement.setLong(1, to - from);
            statement.setLong(2, from);
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Result result = new Result();
                for(String name : columnNames){
                    Object value = resultSet.getObject(name);
                    System.out.println(value.getClass().getName());
                    result.put(name, value);
                }
            }
        } catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return res;
    }

    public List<Result> getValues(String tableName, String[] columnNames){
        List<Result> res = new ArrayList<>();
        try(Connection con = dataSource.getConnection()){
            PreparedStatement statement = con.prepareStatement(String.format("SELECT %s FROM %s", String.join(", ", columnNames), tableName));
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()){
                Result result = new Result();
                for(String name : columnNames){
                    Object value = resultSet.getObject(name);
                    System.out.println(value.getClass().getName());
                    result.put(name, value);
                }
                res.add(result);
            }
        } catch (SQLException e){
            e.printStackTrace(System.err);
        }
        return res;
    }
}
