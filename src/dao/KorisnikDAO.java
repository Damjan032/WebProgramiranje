package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import komunikacija.KorisnikTrans;
import models.Korisnik;
import models.KorisnikNalog;
import models.enums.Uloga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class KorisnikDAO {
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/korisnici.json";


    private ArrayList<KorisnikNalog> kreirajPodatke(){
        var list = new ArrayList<KorisnikNalog>();
        list.add(new KorisnikNalog(new Korisnik("superadmin",Uloga.SUPER_ADMIN),"superadmin".hashCode()));
        try {
            upisListeUFile(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<KorisnikNalog> fetchAll() throws FileNotFoundException {
        return ucitajIzFajla();
    }
    public KorisnikNalog fetchByEmail(String ime) throws IOException {
        var res = ucitajIzFajla();
        if(res.isEmpty()){
            res = kreirajPodatke();
        }
        return res.stream().
                filter(kn -> kn.getKorisnik().getEmail().equals(ime)).
                findFirst().orElse(null);
    }


    public KorisnikNalog create(KorisnikTrans korisnik) throws IOException {
        KorisnikNalog k = new KorisnikNalog(korisnik.getEmail(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getOrganizacija(), Uloga.fromString(korisnik.getUloga()), korisnik.getSifra());
        List<KorisnikNalog> list = fetchAll();
        list.add(k);
        upisListeUFile(list);
        return k;
    }

    public KorisnikNalog update(KorisnikNalog noviKorisnikNalog) throws IOException {
        List<KorisnikNalog> korisnici = fetchAll();
        Korisnik korisnik = noviKorisnikNalog.getKorisnik();
        KorisnikNalog korisnikNalog = korisnici.stream().filter(kn-> kn.getKorisnik().getEmail().equals(korisnik.getEmail())).findFirst().orElse(null);
        if(korisnikNalog != null){
            Korisnik k = korisnikNalog.getKorisnik();
            if(checkStringAttribute(korisnik.getIme())){
                k.setIme(korisnik.getIme());
            }
            if (checkStringAttribute(korisnik.getPrezime())) {
                k.setPrezime(korisnik.getPrezime());
            }
            if(korisnik.getUloga()!=null) {
                k.setUloga(korisnik.getUloga());
            }
            if(checkStringAttribute(korisnik.getOrganizacija())) {
                k.setOrganizacija(korisnik.getOrganizacija());
            }
            if (noviKorisnikNalog.getSifraHash()!=null){
                korisnikNalog.setSifraHash(noviKorisnikNalog.getSifraHash());
            }
        }

        upisListeUFile(korisnici);
        return korisnikNalog;
    }


    private boolean checkStringAttribute(String ime) {
        return ime!=null&& !ime.equals("");
    }

    public List<KorisnikNalog> delete(String id) throws IOException {
        List<KorisnikNalog> korisnici = fetchAll().stream()
                .filter((element) -> !element.getKorisnik().getEmail().equals(id))
                .collect(Collectors.toList());
        upisListeUFile(korisnici);
        return korisnici;
    }

    private void upisListeUFile(List<KorisnikNalog> korisnici) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(korisnici).getBytes());
    }
    private List<KorisnikNalog> ucitajIzFajla(){
        try {

            File f = new File(FILE_PATH);
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    System.out.println("NEMOGUĆE JE KREIRATI  FILE: "+FILE_PATH);
                } else {
                    kreirajPodatke();
                }
            }
            JsonReader reader = new JsonReader(new FileReader(f));
            List<KorisnikNalog> res = g.fromJson(reader, new TypeToken<List<KorisnikNalog>>() {}.getType());
            if (res == null || res.isEmpty()){
                return kreirajPodatke();
            }else{
                return res;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    private Korisnik mapToKorisnik(KorisnikNalog korisnikNalog) {
        return korisnikNalog.getKorisnik();
    }

}
