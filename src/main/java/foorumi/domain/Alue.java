/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.domain;

import foorumi.database.AlueDao;
import foorumi.database.Database;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *
 * @author josujosu
 */
public class Alue {
    
    private Integer id;
    private String aikaleima;
    private String nimi;
    
    public Alue(Integer id, String nimi, String aikaleima){
        this.id = id;
        this.aikaleima = aikaleima;
        this.nimi = nimi;
    }
    public Alue(String nimi){
        this(null, nimi, null);
    }

    public Integer getId() {
        return id;
    }

    public String getNimi() {
        return nimi;
    }

    public String getAikaleima() {
        return aikaleima;
    }
    
    public Integer getViestit() throws Exception{
        AlueDao dao = new AlueDao(new Database("jdbc:sqlite:foorumi.db"));
        return dao.countViestit(id);
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public void setAikaleima(String aikaleima) {
        this.aikaleima = aikaleima;
    }

    @Override
    public String toString() {
        return this.id + " | " + this.nimi + " | " + this.aikaleima;
    }
    
    
}
