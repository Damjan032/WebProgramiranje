package services;

import com.google.gson.Gson;
import dao.KorisnikDAO;
import dto.KorisnikDTO;
import exceptions.NotFoundException;
import komunikacija.LoginPoruka;
import komunikacija.LoginPorukaDTO;
import komunikacija.Poruka;
import models.Korisnik;
import models.KorisnikNalog;
import spark.Request;
import spark.Session;

import java.io.IOException;
import java.util.*;

public class LoginService {


    Gson g = new Gson();
    Set<String> logovaniKorisnici = new HashSet<>();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();



    public KorisnikDTO getKorisnik(Request req){
        Korisnik k = req.session().attribute("korisnik");
        if(k==null){
            return null;
        }
        return KorisnikService.mapKorisniktoKorisnikDTO(k);
    }

    public LoginPorukaDTO tryLogin(Request req) throws IOException {
        Session session = req.session();
        if (session == null) {
            session = req.session(true);
        }
        if(session.attribute("korisnik")!=null){
            return LoginPorukaDTO.mapLPtoLPDTO(new LoginPoruka("Već ste prijavljeni", false));
        }
        String kime = req.queryParams("kime");
        String sifra = req.queryParams("sifra");
        LoginPoruka lp = login(kime, sifra);
        if(lp.isStatus()) {
            session.attribute("korisnik", lp.getKorisnik());
        }
        return LoginPorukaDTO.mapLPtoLPDTO(lp);
    }

    private LoginPoruka login(String kime, String sifra) {
    	KorisnikNalog  kn;
    	try {
        kn = korisnikDAO.fetchByEmail(kime);
    	}catch(NotFoundException nfe) {
            return new LoginPoruka("Ne postoji korisnik sa unetom email adresom!", false);
    	}
        if(logovaniKorisnici.contains(kime)){
            return new LoginPoruka("Već ste prijavljeni!", false);
        }
        if(sifra.hashCode() == kn.getSifraHash()){

            return new LoginPoruka("Uspešna prijava.", true, kn.getKorisnik());
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
