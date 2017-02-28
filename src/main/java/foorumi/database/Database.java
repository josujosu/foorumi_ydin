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
        
        init();
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

    private void init() {
        List<String> lauseet = null;
        if (this.databaseAddress.contains("postgres")) {
            lauseet = postgreLauseet();
        } else {
            lauseet = sqliteLauseet();
        }

        // "try with resources" sulkee resurssin automaattisesti lopuksi
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();

            // suoritetaan komennot
            for (String lause : lauseet) {
                System.out.println("Running command >> " + lause);
                st.executeUpdate(lause);
            }

        } catch (Throwable t) {
            // jos tietokantataulu on jo olemassa, ei komentoja suoriteta
            System.out.println("Error >> " + t.getMessage());
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.databaseAddress.contains("postgres")) {
            try {
                URI dbUri = new URI(databaseAddress);

                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

                return DriverManager.getConnection(dbUrl, username, password);
            } catch (Throwable t) {
                System.out.println("Error: " + t.getMessage());
                t.printStackTrace();
            }
        }

        return DriverManager.getConnection(databaseAddress);
    }

    private List<String> postgreLauseet() {
        ArrayList<String> lista = new ArrayList<>();

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("DROP TABLE Tuote;");
        // heroku käyttää SERIAL-avainsanaa uuden tunnuksen automaattiseen luomiseen
        lista.add("CREATE TABLE Alue (id SERIAL PRIMARY KEY, nimi varchar(70) NOT NULL UNIQUE, aikaleima datetime);");
        lista.add("CREATE TABLE Keskustelu (id SERIAL PRIMARY KEY, nimi varchar(70) NOT NULL UNIQUE, aikaleima datetime);");
        
        return lista;
    }

    private List<String> sqliteLauseet() {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("DROP TABLE Alue;");
        lista.add("DROP TABLE Keskustelu;");
        lista.add("DROP TABLE Viesti;");

        // tietokantataulujen luomiseen tarvittavat komennot suoritusjärjestyksessä
        lista.add("CREATE TABLE Alue (id integer PRIMARY KEY, nimi varchar (70) NOT NULL UNIQUE, aikaleima datetime);");
        lista.add("CREATE TABLE Keskustelu (id integer PRIMARY KEY, aikaleima datetime, otsikko varchar (70) NOT NULL UNIQUE, alue integer NOT NULL,  FOREIGN KEY(alue) REFERENCES Alue(id));");
        lista.add("CREATE TABLE Viesti (id integer PRIMARY KEY, sisalto varchar NOT NULL, keskustelu integer NOT NULL, lahettaja varchar(40), FOREIGN KEY (keskustelu) REFERENCES  Keskustelu(id));");

        return lista;
    }
}
