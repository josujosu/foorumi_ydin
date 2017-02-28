/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kayttoliittyma;

import foorumi.database.AlueDao;
import foorumi.database.Database;
import foorumi.database.KeskusteluDao;
import foorumi.database.ViestiDao;
import foorumi.domain.Alue;
import foorumi.domain.Keskustelu;
import foorumi.domain.Viesti;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author josujosu
 */
public class Kayttoliittyma {

    private Database database;
    private AlueDao alueDao;
    private KeskusteluDao keskusteluDao;
    private ViestiDao viestiDao;
    private Scanner lukija;

    public Kayttoliittyma() throws Exception {
        this.database = new Database("jdbc:sqlite:foorumi.db");
        this.alueDao = new AlueDao(database);
        this.keskusteluDao = new KeskusteluDao(database);
        this.viestiDao = new ViestiDao(database);
        this.lukija = new Scanner(System.in);
    }

    public void run() throws SQLException {
//        System.out.println("toimiiko");
//        this.viestiDao.save(new Viesti("toimiiko?", 1));
//        for (Alue alue : alueet()) {
//            System.out.println(alue.getNimi());
//            System.out.println(alue.getAikaleima());
//            System.out.println(alue.getId());
//        }
//        for (Keskustelu alue : keskustelut()) {
//            System.out.println(alue.getOtsikko());
//            System.out.println(alue.getAikaleima());
//            System.out.println(alue.getId());
//        }    
        System.out.println(alueDao.countViestit(1));
        while (true) {
            System.out.println("\n\nTulosta alueet - alu\nTulosta keskustelut - kesk\nTulosta viestit - vie\nUusi alue - uusia\nUusi keskustelu - uusik\nUusi viesti - uusiv\nListaa alueen keskustelut - ak\nListaa keskustelun viestit - kv\nLopeta - x");
            System.out.println("Anna komento:");
            System.out.print("> ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                System.out.println("Näkemiin!");
                break;
            }
            if (komento.equals("alu")) {
                tulostaAlueet();
            }
            if (komento.equals("kesk")) {
                tulostaKeskustelut();
            }
            if (komento.equals("vie")) {
                tulostaViestit();
            }
            if (komento.equals("uusia")) {
                uusiAlue();
            }
            if (komento.equals("uusik")) {
                uusiKeskustelu();
            }
            if (komento.equals("uusiv")) {
                uusiViesti();
            }
            if (komento.equals("ak")) {
                alueenKeskustelut();
            }
            if (komento.equals("kv")) {
                keskustelunViestit();
            }

        }
    }

    public void tulostaAlueet() throws SQLException {
        for (Alue alue : alueet()) {
            System.out.println(alue);
        }
    }

    public List<Alue> alueet() throws SQLException {
        return alueDao.findAll();
    }

    public void tulostaKeskustelut() throws SQLException {
        for (Keskustelu k : keskustelut()) {
            System.out.println(k);
        }
    }

    public List<Keskustelu> keskustelut() throws SQLException {
        return keskusteluDao.findAll();
    }

    public List<Viesti> viestit() throws SQLException {
        return this.viestiDao.findAll();
    }

    public void tulostaViestit() throws SQLException {
        for (Viesti v : viestit()) {
            System.out.println(v);
        }
    }

    public void uusiAlue() throws SQLException {
        System.out.print("Nimi? ");
        String nimi = lukija.nextLine();
        alueDao.save(new Alue(nimi));
        System.out.println("Alue ehkä lisätty jos onnistui lol");
    }

    public void uusiKeskustelu() throws SQLException {
        System.out.print("Otsikko? ");
        String otsikko = lukija.nextLine();
        System.out.print("Anna alueen id? ");
        Integer alue = Integer.parseInt(lukija.nextLine());
        keskusteluDao.save(new Keskustelu(otsikko, alue));
        System.out.println("Onnistui ehkä en tiedä");
    }

   public void uusiViesti() throws SQLException {
//        System.out.print("Viesti? ");
//        String viesti = lukija.nextLine();
//        System.out.print("Keskustelun id? ");
//        Integer kesku = Integer.parseInt(lukija.nextLine());
//        viestiDao.save(new Viesti(viesti, kesku));
//        System.out.println("En tiedä onnistuiko :(");
    }

    public void alueenKeskustelut() throws SQLException {
        System.out.print("Alue? ");
        Integer alue = Integer.parseInt(lukija.nextLine());
        for (Keskustelu kesk : keskusteluDao.findAllFromAlue(alue)) {
            System.out.println(kesk);
        }
    }

    public void keskustelunViestit() throws SQLException {
        System.out.println("Keskustelu? ");
        Integer kesk = Integer.parseInt(lukija.nextLine());
        for (Viesti viesti : viestiDao.findAllFromKeskustelu(kesk)) {
            System.out.println(viesti);
        }
    }
}
