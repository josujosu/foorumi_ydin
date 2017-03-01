/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package foorumi;

import foorumi.database.AlueDao;
import foorumi.database.Database;
import foorumi.database.KeskusteluDao;
import foorumi.database.ViestiDao;
import foorumi.domain.Alue;
import foorumi.domain.Keskustelu;
import foorumi.domain.Viesti;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kayttoliittyma.Kayttoliittyma;
import spark.Spark;
import static spark.Spark.port;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

/**
 *
 * @author josujosu
 */
public class Foorumi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
//        Kayttoliittyma run = new Kayttoliittyma();
//        System.out.println("toimiiko");
//        run.run();

        // asetetaan portti jos heroku antaa PORT-ympäristömuuttujan
        if (System.getenv("PORT") != null) {
            port(Integer.valueOf(System.getenv("PORT")));
        }

        // käytetään oletuksena paikallista sqlite-tietokantaa
        String jdbcOsoite = "jdbc:sqlite:kanta.db";
        // jos heroku antaa käyttöömme tietokantaosoitteen, otetaan se käyttöön
        if (System.getenv("DATABASE_URL") != null) {
            jdbcOsoite = System.getenv("DATABASE_URL");
        }

        Database db = new Database(jdbcOsoite);

        System.out.println("");
        Database database = new Database("jdbc:sqlite:foorumi.db");
        AlueDao alueDao = new AlueDao(database);
        KeskusteluDao keskusteluDao = new KeskusteluDao(database);
        ViestiDao viestiDao = new ViestiDao(database);

        Spark.get("/", (req, res) -> {
            res.redirect("/alueet");
            return "ok";
        });

        Spark.get("/alueet", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("alueet", alueDao.findAll());
            return new ModelAndView(data, "alueet");
        }, new ThymeleafTemplateEngine());

        Spark.get("/alueet/:a_id", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("keskustelut", keskusteluDao.findTenLastFromAlue(Integer.parseInt(req.params(":a_id"))));
            data.put("alue", alueDao.findOne(Integer.parseInt(req.params(":a_id"))));
            return new ModelAndView(data, "keskustelut");
        }, new ThymeleafTemplateEngine());

        Spark.get("/alueet/:a_id/:k_id/:sivu", (req, res) -> {
            HashMap data = new HashMap<>();
            data.put("viestit", viestiDao.findFiftyFromSivu(Integer.parseInt(req.params(":sivu")), Integer.parseInt(req.params(":k_id"))));
            data.put("keskustelu", keskusteluDao.findOne(Integer.parseInt(req.params(":k_id"))));
            data.put("alue", alueDao.findOne(Integer.parseInt(req.params(":a_id"))));
            data.put("sivut", keskusteluDao.pageList(keskusteluDao.countPages(Integer.parseInt(req.params(":k_id")))));
            Integer lukualoitus = 1 + (Integer.parseInt(req.params(":sivu")) - 1) * 20;
            data.put("startingpoint", lukualoitus);
            return new ModelAndView(data, "viestit");
        }, new ThymeleafTemplateEngine());

        Spark.post("/alueet", (req, res) -> {
            String onkotyhja = req.queryParams("nimi").trim();
            if (!onkotyhja.isEmpty()) {
                alueDao.save(new Alue(onkotyhja));
            }
            res.redirect("/");
            return "ok";
        });

        Spark.post("/alueet/:a_id", (req, res) -> {
            String onkotyhja = req.queryParams("otsikko").trim();
            if (!onkotyhja.isEmpty()) {
                keskusteluDao.save(new Keskustelu(onkotyhja, Integer.parseInt(req.params(":a_id"))));
            }
            res.redirect("/alueet/" + req.params(":a_id"));
            return "ok";
        });

        Spark.post("/alueet/:a_id/:k_id/:sivu", (req, res) -> {
            viestiDao.save(new Viesti(req.queryParams("sisalto"), Integer.parseInt(req.params(":k_id")), req.queryParams("lahettaja")));
            res.redirect("/alueet/" + req.params(":a_id") + "/" + req.params(":k_id") + "/" + req.params("sivu"));
            return "ok";
        });
    }

}
