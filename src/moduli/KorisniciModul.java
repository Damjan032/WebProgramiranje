package moduli;

import models.Korisnik;
import models.KorisnikNalog;
import models.enums.Uloga;
import komunikacija.KorisnikTrans;
import komunikacija.Poruka;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KorisniciModul {

    private HashMap<String, KorisnikNalog> korisniciNalozi = new HashMap<>();
    private List<Korisnik> korisnici = new ArrayList<>();


    public KorisniciModul() {
        Korisnik sadmin = new Korisnik(Uloga.SUPER_ADMIN);
        korisniciNalozi.put("superadmin", new KorisnikNalog(sadmin, "superadmin".hashCode()));


        //ZA TESTIRANJEE
        dodajKorisnika( sadmin, new KorisnikTrans("mika","mika", "mikic", "org", "mika","admin"));
        dodajKorisnika( sadmin, new KorisnikTrans("pera","pera", "pera", "org", "pera","korisnik"));
        dodajKorisnika( sadmin, new KorisnikTrans("djura","djura", "djuric", "org", "djura","korisnik"));

    }

    public boolean korisnikRegistrovan(String korisnickoIme){
        return korisniciNalozi.containsKey(korisnickoIme);
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

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public Poruka dodajKorisnika(Korisnik user, KorisnikTrans k) {
        if (user.getUloga()==Uloga.KORISNIK){
            return new Poruka("Niste ovlašćeni za ovu akciju!",false);
        }
        if (k.getUloga()==null){
            return new Poruka("Niste odabrali nijednu organizaciju!",false);
        }
        Uloga u;
        switch (k.getUloga()){
            case "admin": {
                u = Uloga.ADMIN;
                break;
            }
            case "korisnik": {
                u = Uloga.KORISNIK;
                break;
            }
            default:
                return new Poruka("Nedozvoljena uloga!",false);
        }
        if(korisnikRegistrovan(k.getEmail())){
            return new Poruka("Korisnik sa email-om "+k.getEmail()+" već postoji!",false);
        }
        Korisnik korisnik = new Korisnik(k.getEmail(), k.getIme(), k.getPrezime(), k.getOrganizacija(), u);
        korisniciNalozi.put(k.getEmail(), new KorisnikNalog(korisnik, k.getSifra().hashCode()));
        korisnici.add(korisnik);
        return new Poruka("Korisnik uspešno dodat",true);

    }

    public KorisnikNalog get(String username) {

        return korisniciNalozi.get(username);
    }

    public Korisnik getKorisnik(Korisnik user, String email) {
        Korisnik nadjeniKorisnik = korisniciNalozi.get(email).getKorisnik();
        if (nadjeniKorisnik.getUloga()==Uloga.SUPER_ADMIN){
            return null;
        }
        if(user.getUloga()==Uloga.ADMIN){
            if(!user.getOrganizacija().equals(nadjeniKorisnik.getOrganizacija())){
                return null;
            }
        }
        return nadjeniKorisnik;
    }

    public Poruka azurirajKorisnika(Korisnik user, KorisnikTrans fromJson) {
        if(!korisnikRegistrovan(fromJson.getEmail())){
            return new Poruka("Korisnik kojeg ste izmenili ne postoji!", false);
        }
        KorisnikNalog izmenjeniKorisnik = korisniciNalozi.get(fromJson.getEmail());
        Korisnik k = izmenjeniKorisnik.getKorisnik();
        if(user.getUloga()==Uloga.ADMIN){
            if(!user.getOrganizacija().equals(k.getOrganizacija())){
                return new Poruka("Ne možete da menjate korisnike iz drugih otrganizacija.",false);
            }
            if (k.getUloga()==Uloga.SUPER_ADMIN ){
                return new Poruka("Ne možete da menjate super admina.",false);
            }
        }else if(user.getUloga()==Uloga.KORISNIK){
            return new Poruka("Niste ovlašćeni za ovu akciju!",false);
        }
        k.setIme(fromJson.getIme());
        k.setPrezime(fromJson.getPrezime());
        k.setUloga(Uloga.fromString(fromJson.getUloga()));
        return new Poruka("Izmena uspešna.", true);
    }
}
