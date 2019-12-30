package controllers;

import com.google.gson.Gson;
import komunikacija.KorisnikTrans;
import komunikacija.Poruka;
import models.Korisnik;
import services.KorisnikService;
import services.OrganizacijaService;
import spark.Session;

import static spark.Spark.*;


public class KorisnikController implements Controller{


    private static Gson g = new Gson();
    KorisnikService korisnikService = new KorisnikService();

    @Override
    public void init() {
        get("/korisnici", (req, res) -> {
            return g.toJson(korisnikService.fetchAll(req,res));
        });

        get("/korisnici/:id", (req, res) -> {
            return g.toJson(korisnikService.fetchById(req, res));
        });

        post("/korisnici", (req, res)->{
            return g.toJson(korisnikService.create(req,res));
        });

        put("/korisnici/:id", (req, res)->{
            return g.toJson(korisnikService.update(req, res));
        });
        delete("/korisnici/:id",(req, res)->{
            return g.toJson(korisnikService.delete(req, res));
        });
    }
}
