package models;

import models.enums.Uloga;
import models.komunikacija.LoginPoruka;

import java.util.HashMap;

public class Sistem {
    private HashMap<String, KorisnikNalog> korisnici = new HashMap<>();



    public Sistem() {
        korisnici.put("superadmin",  new KorisnikNalog(new Korisnik(Uloga.SUPER_ADMIN),"superadmin".hashCode()));
    }


    public LoginPoruka login(String username, String password){
        if(!korisnici.containsKey(username)){
            return new LoginPoruka("Korisnik sa korisničkim imenom: \""+username+"\" ne postoji", false);
        }
        KorisnikNalog kn =korisnici.get(username);
        if (kn.getSifraHash() == password.hashCode()){
            return  new LoginPoruka("Uspešno prijavljicanje", true, kn.getKorisnik());
        }

        return  new LoginPoruka("Pogrešna šifra!", false);
    }

}
