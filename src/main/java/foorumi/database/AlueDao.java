/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database;

import foorumi.database.collector.AlueCollector;
import foorumi.domain.Alue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author josujosu
 */
public class AlueDao implements Dao<Alue, Integer> {

    private Database database;

    public AlueDao(Database database) {
        this.database = database;
    }

    @Override
    public Alue findOne(Integer key) throws SQLException {
        List<Alue> alue = this.database.queryAndCollect("SELECT * FROM Alue WHERE id = ?", new AlueCollector(), key);
        if (alue.isEmpty()) {
            return null;
        }
        return alue.get(0);
    }

    @Override
    public void save(Alue alue) throws SQLException {
        this.database.update("INSERT INTO Alue (nimi) VALUES (?)", alue.getNimi());
    }

    @Override
    public List<Alue> findAll() throws SQLException {
        return this.database.queryAndCollect("SELECT * FROM Alue ORDER BY nimi ASC", new AlueCollector());
        
    }

    @Override
    public void delete(Integer key) throws SQLException {
        this.database.update("DELETE FROM Alue WHERE id = ?", key);
    }
    public Integer countViestit(Integer key) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:foorumi.db");
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) as maara FROM Alue a, Keskustelu k, Viesti v WHERE a.id = k.alue AND k.id = v.keskustelu AND a.id = ?;");
        stmt.setInt(1, key);
        ResultSet result = stmt.executeQuery();
        Integer lkm = 0;
        while(result.next()){
            lkm = Integer.parseInt(result.getString("maara"));
        }
        conn.close();
        return lkm;
    }

}
