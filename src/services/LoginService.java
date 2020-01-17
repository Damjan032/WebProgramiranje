package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dao.KorisnikDAO;
import komunikacija.LoginPoruka;
import komunikacija.Poruka;
import models.Korisnik;
import models.KorisnikNalog;
import spark.Request;
import spark.Session;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LoginService {


    Gson g = new Gson();
    Set<String> logovaniKorisnici = new HashSet<>();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();



    public Korisnik getKorisnik(Request req){
        return req.session().attribute("korisnik");
    }

    public LoginPoruka tryLogin(Request req) throws IOException {
        Session session = req.session();
        if (session == null) {
            session = req.session(true);
        }
        if(session.attribute("korisnik")!=null){
            return new LoginPoruka("Već ste prijavljeni", false);
        }
        String kime = req.queryParams("kime");
        String sifra = req.queryParams("sifra");
        LoginPoruka lp = login(kime, sifra);
        if(lp.isStatus()) {
            session.attribute("korisnik", lp.getKorisnik());
        }
        return lp;
    }

    private LoginPoruka login(String kime, String sifra) throws IOException {
        KorisnikNalog kn = korisnikDAO.fetchByEmail(kime);
        if (kn==null){
            return new LoginPoruka("Ne postoji korisnik sa unetom email adresom!", false);
        }
        if(logovaniKorisnici.contains(kime)){
            return new LoginPoruka("Već ste prijavljeni!", false);
        }
        if(sifra.hashCode() == kn.getSifraHash()){

            return new LoginPoruka("Uspešna prijava.", true, korisnikDAO.fetchByEmail(kime).getKorisnik());
        }
        return new LoginPoruka("Pogrešna šifra!", false);

    }

    public boolean isLoggedin(String email){
        return logovaniKorisnici.contains(email);
    }

    public Poruka tryLogout(Request req) {
        Session session = req.session();
        Korisnik korisnik = session.attribute("korisnik");

        if(korisnik==null){
            return new Poruka("Već ste odjavljeni!", false);
        }
        logovaniKorisnici.remove(korisnik.getEmail());
        session.removeAttribute("user");
        session.invalidate();
        return new Poruka("Odjava uspešna.", true);
    }
}
