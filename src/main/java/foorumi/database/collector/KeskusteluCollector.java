/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database.collector;

import foorumi.database.Collector;
import foorumi.domain.Alue;
import foorumi.domain.Keskustelu;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author josujosu
 */


public class KeskusteluCollector implements Collector<Keskustelu> {

    

    @Override
    public Keskustelu collect(ResultSet rs) throws SQLException {
        return new Keskustelu(rs.getInt("id"), rs.getString("otsikko"), rs.getString("aikaleima"), rs.getInt("alue"));
    }
    
}
