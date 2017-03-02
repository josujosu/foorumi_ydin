/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database;

import foorumi.database.collector.KeskusteluCollector;
import foorumi.domain.Keskustelu;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author josujosu
 */
public class KeskusteluDao implements Dao<Keskustelu, Integer> {

    private Database db;

    public KeskusteluDao(Database db) {
        this.db = db;
    }

    @Override
    public Keskustelu findOne(Integer key) throws SQLException {
        List<Keskustelu> keskustelu = this.db.queryAndCollect("SELECT * FROM Keskustelu WHERE id = ?", new KeskusteluCollector(), key);
        if (keskustelu.isEmpty()) {
            return null;
        }
        return keskustelu.get(0);
    }

    @Override
    public void save(Keskustelu keskustelu) throws SQLException {
        this.db.update("INSERT INTO Keskustelu(otsikko,alue) VALUES(?,?);", keskustelu.getOtsikko(), keskustelu.getAlue());
    }

    @Override
    public List<Keskustelu> findAll() throws SQLException {
        List<Keskustelu> kesk = this.db.queryAndCollect("SELECT * FROM Keskustelu", new KeskusteluCollector());
        return kesk;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        this.db.update("DELETE FROM Keskustelu WHERE id = ?", key);
    }

    public List<Keskustelu> findAllFromAlue(Integer key) throws SQLException {
        List<Keskustelu> kesk = this.db.queryAndCollect("SELECT * FROM Keskustelu WHERE alue = ?", new KeskusteluCollector(), key);
        return kesk;
    }
    
    public Integer findIdWithOtsikko(String otsikko) throws SQLException {
        List<Keskustelu> iideet = this.db.queryAndCollect("SELECT * FROM Keskustelu WHERE otsikko = ? ORDER BY id DESC", new KeskusteluCollector(), otsikko);
        return iideet.get(0).getId();
    }

    public Integer countViestit(Integer key) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:foorumi.db");
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS maara FROM Viesti WHERE keskustelu = ?");
        stmt.setInt(1, key);
        ResultSet result = stmt.executeQuery();
        Integer lkm = 0;
        while (result.next()) {
            lkm = Integer.parseInt(result.getString("maara"));
        }
        conn.close();
        return lkm;
    }

    public List<Keskustelu> findTenLastFromAlue(Integer key) throws SQLException {
        List<Keskustelu> kesk = db.queryAndCollect("SELECT * FROM Keskustelu WHERE alue=? ORDER BY aikaleima DESC LIMIT 10;", new KeskusteluCollector(), key);
        return kesk;
    }

    public Integer countPages(Integer key) throws SQLException {
        Integer lkm = countViestit(key);
        if (lkm == 0) {
            return 1;
        }
        int sivumaara = 0;
        while (lkm > 0) {
            lkm = lkm - 20;
            sivumaara++;
        }
        return sivumaara;
    }
    
    public ArrayList<Integer> pageList(Integer sivumaara){
        ArrayList<Integer> sivut = new ArrayList<>();
        for(int i = 1; i <= sivumaara; i++){
            sivut.add(i);
        }
        return sivut;
    }

}
