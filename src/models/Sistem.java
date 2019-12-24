package models;

import models.enums.Uloga;
import models.komunikacija.LoginPoruka;
import models.moduli.OrganizacijeModul;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Sistem {
    private HashMap<String, KorisnikNalog> korisniciNalozi = new HashMap<>();
    private OrganizacijeModul orgController = OrganizacijeModul.getInstance();
    private List<Korisnik> korisnici = new ArrayList<>();

    public OrganizacijeModul getOrgController() {
        return orgController;
    }

    public Sistem() {
        korisniciNalozi.put("superadmin", new KorisnikNalog(new Korisnik(Uloga.SUPER_ADMIN), "superadmin".hashCode()));
        korisnici.add(new Korisnik("a dsd", "sdsad ", "asd sad", " sadasd", Uloga.KORISNIK, null));
    }


    public LoginPoruka login(String username, String password) {
        if (!korisniciNalozi.containsKey(username)) {
            return new LoginPoruka("Korisnik sa korisničkim imenom: \"" + username + "\" ne postoji", false);
        }
        KorisnikNalog kn = korisniciNalozi.get(username);
        if (kn.getSifraHash() == password.hashCode()) {
            return new LoginPoruka("Uspešno prijavljicanje", true, kn.getKorisnik());
        }

        return new LoginPoruka("Pogrešna šifra!", false);
    }

    public List<Korisnik> getKorisnici(Korisnik user) {
        switch (user.getUloga()) {

            case SUPER_ADMIN:
                return korisnici;
            case ADMIN:
                return korisnici.stream().
                        filter(k -> k.getOrganizacija().equals(user.getOrganizacija())).
                        collect(Collectors.toList());
            default:
                return null;
        }
    }
}
