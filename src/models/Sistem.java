package models;

import models.moduli.OrganizacijeModul;
import models.enums.Uloga;
import models.komunikacija.LoginPoruka;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Sistem {
    private HashMap<String, KorisnikNalog> korisnici = new HashMap<>();
    private OrganizacijeModul orgController = OrganizacijeModul.getInstance();
    private HashMap<String, KorisnikNalog> korisniciNalozi = new HashMap<>();

    public OrganizacijeModul getOrgController() {
        return orgController;
    }

    public Sistem() {
        korisniciNalozi.put("superadmin",  new KorisnikNalog(new Korisnik(Uloga.SUPER_ADMIN),"superadmin".hashCode()));
    }



    public LoginPoruka login(String username, String password){
        if(!korisniciNalozi.containsKey(username)){
            return new LoginPoruka("Korisnik sa korisničkim imenom: \""+username+"\" ne postoji", false);
        }
        KorisnikNalog kn = korisniciNalozi.get(username);
        if (kn.getSifraHash() == password.hashCode()){
            return  new LoginPoruka("Uspešno prijavljicanje", true, kn.getKorisnik());
        }

        return  new LoginPoruka("Pogrešna šifra!", false);
    }

    public List<Korisnik> getKorisnici(Korisnik user) {
        switch (user.getUloga()){

            case SUPER_ADMIN:
                return korisnici;
            case ADMIN:
                return korisnici.stream().
                        filter(k->k.getOrganizacija().equals(user.getOrganizacija())).
                        collect(Collectors.toList());
            default:
                return null;
        }
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
