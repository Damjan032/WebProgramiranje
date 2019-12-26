package models;

import models.enums.Uloga;
import models.komunikacija.KorisnikTrans;
import models.komunikacija.LoginPoruka;
import models.komunikacija.Poruka;
import models.moduli.KorisniciModul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Sistem {

    private KorisniciModul korisniciModul;



    public Sistem(){
        korisniciModul = new KorisniciModul();
    }

    public LoginPoruka login(String username, String password) {
        if (!korisniciModul.korisnikRegistrovan(username)) {
            return new LoginPoruka("Korisnik sa korisničkim imenom: \"" + username + "\" ne postoji", false);
        }
        KorisnikNalog kn = korisniciModul.get(username);
        if (kn.getSifraHash() == password.hashCode()) {
            return new LoginPoruka("Uspešno prijavljicanje", true, kn.getKorisnik());
        }

        return new LoginPoruka("Pogrešna šifra!", false);
    }

    public List<Korisnik> getKorisnici(Korisnik user) {
        return korisniciModul.getKorisnici();
    }

    public Poruka dodajKorisnika(Korisnik user, KorisnikTrans kt) {
        return korisniciModul.dodajKorisnika(user,kt);
    }

    public KorisniciModul getKorisniciModul() {
        return korisniciModul;
    }

    public void setKorisniciModul(KorisniciModul korisniciModul) {
        this.korisniciModul = korisniciModul;
    }
}
