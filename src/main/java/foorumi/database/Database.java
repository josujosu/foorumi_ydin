/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database;

/**
 *
 * @author josujosu
 */
import java.net.URI;
import java.sql.*;
import java.util.*;

public class Database {

    private Connection connection;
    private String databaseAddress;

    public Database(String address) throws Exception {
        this.connection = DriverManager.getConnection(address);
        this.databaseAddress = address;
        
        //init();
    }

    public <T> List<T> queryAndCollect(String query, Collector<T> col, Object... params) throws SQLException {
        List<T> rivit = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            rivit.add(col.collect(rs));
        }

        rs.close();
        stmt.close();
        return rivit;
    }

    public int update(String updateQuery, Object... params) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(updateQuery);

        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        int changes = stmt.executeUpdate();

        stmt.close();

        return changes;
    }


}
