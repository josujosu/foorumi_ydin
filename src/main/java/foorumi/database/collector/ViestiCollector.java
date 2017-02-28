/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi.database.collector;

import foorumi.database.Collector;
import foorumi.domain.Viesti;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author josujosu
 */
public class ViestiCollector implements Collector<Viesti>{
    
     @Override
    
    public Viesti collect(ResultSet rs) throws SQLException {
        return new Viesti(rs.getInt("id"), rs.getString("sisalto"), rs.getInt("keskustelu"), rs.getString("lahettaja"));
    }
    
    
}
