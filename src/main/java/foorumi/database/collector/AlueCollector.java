/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database.collector;

import foorumi.database.Collector;
import foorumi.domain.Alue;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author josujosu
 */


public class AlueCollector implements Collector<Alue> {

    

    @Override
    public Alue collect(ResultSet rs) throws SQLException {
        return new Alue(rs.getInt("id"), rs.getString("nimi"), rs.getString("aikaleima"));
    }
    
}
