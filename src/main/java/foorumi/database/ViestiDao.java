/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database;

import foorumi.database.collector.ViestiCollector;
import foorumi.domain.Viesti;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author josujosu
 */
public class ViestiDao implements Dao<Viesti, Integer> {

    private Database db;

    public ViestiDao(Database db) {
        this.db = db;
    }

    @Override
    public Viesti findOne(Integer key) throws SQLException {
        List<Viesti> viesti = this.db.queryAndCollect("SELECT * FROM Viesti WHERE id = ?", new ViestiCollector(), key);
        if (viesti.isEmpty()) {
            return null;
        }
        return viesti.get(0);
    }

    @Override
    public void save(Viesti viesti) throws SQLException {
        if (viesti.getLahettaja().equals("")) {
            this.db.update("INSERT INTO Viesti(sisalto, keskustelu, lahettaja) VALUES(?,?,?);", viesti.getSisalto(), viesti.getKeskustelu(), "Anonyymi");
        } else {
            this.db.update("INSERT INTO Viesti(sisalto, keskustelu, lahettaja) VALUES(?,?,?);", viesti.getSisalto(), viesti.getKeskustelu(), viesti.getLahettaja());
        }
        this.db.update("UPDATE Keskustelu SET aikaleima = (datetime('now', 'localtime')) WHERE id = ?", viesti.getKeskustelu());
        this.db.update("UPDATE Alue SET aikaleima = (datetime('now', 'localtime')) WHERE id = (SELECT alue FROM Keskustelu WHERE id = ?)", viesti.getKeskustelu());
    }

    @Override
    public List<Viesti> findAll() throws SQLException {
        List<Viesti> viesti = this.db.queryAndCollect("SELECT * FROM Viesti", new ViestiCollector());
        return viesti;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        this.db.update("DELETE FROM Viesti WHERE id = ?", key);
    }

    public List<Viesti> findAllFromKeskustelu(Integer key) throws SQLException {
        List<Viesti> viestit = this.db.queryAndCollect("SELECT * FROM Viesti WHERE keskustelu = ?", new ViestiCollector(), key);
        return viestit;
    }

    public List<Viesti> findFiftyFromSivu(Integer sivu, Integer kesk) throws SQLException {
        List<Viesti> viestit = this.db.queryAndCollect("SELECT * FROM Viesti WHERE keskustelu = ? LIMIT 20 OFFSET (0 + ((? - 1) * 20))", new ViestiCollector(), kesk, sivu);
        return viestit;
    }

}
