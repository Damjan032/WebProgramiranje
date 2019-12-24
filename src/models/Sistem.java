package models;

import controllers.OrganizacijaController;
import models.enums.Uloga;
import models.komunikacija.LoginPoruka;
import models.komunikacija.Poruka;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Sistem {
    private HashMap<String, KorisnikNalog> korisnici = new HashMap<>();
    private OrganizacijaController orgController = OrganizacijaController.getInstance();

    public OrganizacijaController getOrgController() {
        return orgController;
    }

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
