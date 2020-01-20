package controllers;

import com.google.gson.Gson;
import services.KorisnikService;

import static spark.Spark.*;


public class KorisnikController implements Controller{


    private static Gson g = new Gson();
    KorisnikService korisnikService = new KorisnikService();

    @Override
    public void init() {
        get("/korisnici", (req, res) -> {
            res.type("application/json");
            return korisnikService.fetchAll(req,res);
        });

        get("/korisnici/:id", (req, res) -> {
            res.type("application/json");
            return korisnikService.fetchById(req);
        });

        post("/korisnici", (req, res)->{
            res.type("application/json");
            return korisnikService.create(req,res);
        });

        put("/korisnici", (req, res)->{
            res.type("application/json");
            return g.toJson(korisnikService.update(req, res));
        });
        delete("/korisnici/:id",(req, res)->{
            res.type("application/json");
            return korisnikService.delete(req, res);
        });
    }
}
