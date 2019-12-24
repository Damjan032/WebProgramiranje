package models;

import models.enums.Uloga;
import models.komunikacija.LoginPoruka;
import models.komunikacija.Poruka;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

public class Sistem {
    private HashMap<String, KorisnikNalog> korisnici = new HashMap<>();
    private ArrayList<Organizacija> organizacije = new ArrayList<>();



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

    public Poruka addOrg(String name, String opis, String imgPath){
        System.out.println("NAAAAAAAAAME" + name);
        for (Organizacija o: organizacije) {
            if (o.getIme().equalsIgnoreCase(name))
                return new Poruka("Organizacija je vec uneta", false);
        };
        organizacije.add(new Organizacija(name,opis,imgPath, new ArrayList<>(), new ArrayList<>()));
        return new Poruka("Uspesno uneta organizacija", true);
    }

}
