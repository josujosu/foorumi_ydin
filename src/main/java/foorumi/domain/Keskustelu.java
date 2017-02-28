/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.domain;

import foorumi.database.Database;
import foorumi.database.KeskusteluDao;
import java.sql.Timestamp;

/**
 *
 * @author josujosu
 */
public class Keskustelu {
    private Integer id;
    private String otsikko;
    private String aikaleima;
    private Integer alue;

    public Keskustelu(Integer id, String otsikko, String aikaleima, Integer alue) {
        this.id = id;
        this.otsikko = otsikko;
        this.aikaleima = aikaleima;
        this.alue = alue;
    }
    public Keskustelu(String otsikko, Integer alue) {
        this(null, otsikko, null , alue);
    }

    public Integer getId() {
        return id;
    }

    public Integer getAlue() {
        return alue;
    }

    public String getAikaleima() {
        return aikaleima;
    }

    public String getOtsikko() {
        return otsikko;
    }
    
    public String getShortotsikko(){
        if(this.otsikko.length() > 25){
            StringBuilder uusi = new StringBuilder("");
            for(int i = 0; i < 25; i++){
                uusi.append(this.otsikko.charAt(i));
            }
            uusi.append("...");
            return uusi.toString();
        }
        return otsikko;
    }
    
    public Integer getViestit() throws Exception{
        KeskusteluDao dao = new KeskusteluDao(new Database("jdbc:sqlite:foorumi.db"));
        return dao.countViestit(id);
    }

    public void setAikaleima(String aikaleima) {
        this.aikaleima = aikaleima;
    }

    public void setAlue(Integer alue) {
        this.alue = alue;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }
    @Override
    public String toString() {
        return this.id + " | " + this.otsikko + " | " + this.aikaleima + " | " + this.alue;
    }
    
    
}
