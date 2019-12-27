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

        get("/tipKorisnika", (req, res) -> {
            Session s = req.session();
            System.out.println(s.attribute("user") != null);
            Korisnik k = s.attribute("user");
            if (k == null) {
                res.status(401);
                return "";
            } else {
                return g.toJson(k.getUloga().toString());
            }
        });
        get("/korisnici", (req, res) -> {
            Session session = req.session();
            Korisnik user = session.attribute("user");
            if(user == null) {
                res.status(401);
                return g.toJson(false);
            }
            return g.toJson(sistem.getKorisnici(user));
        });

        get("/korisnici/:email", (req, res) -> {
            Session session = req.session();
            Korisnik user = session.attribute("user");
            if(user == null) {
                return g.toJson(false);
            }
            String email = req.params(":email");
            return g.toJson(sistem.getKorisnik(user,email));
        });

        post("/korisnici", (req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.dodajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        });
        put("/korisnici", (req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.azurirajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        });
        delete("/korisnici",(req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.azurirajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        }
    });
}
