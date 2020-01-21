package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import komunikacija.KorisnikTrans;
import models.Disk;
import models.Korisnik;
import models.KorisnikNalog;
import models.Organizacija;
import models.enums.Uloga;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class KorisnikDAO extends Initializer{
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/korisnici.json";

    @Override
    Object generateData() {
        var list = new ArrayList<KorisnikNalog>();
        list.add(new KorisnikNalog(new Korisnik("superadmin",Uloga.SUPER_ADMIN),"superadmin".hashCode()));
        try {
            upisListeUFile(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    protected List<Object> readData() throws FileNotFoundException {
        return g.fromJson(new JsonReader(new FileReader(FILE_PATH)),new TypeToken<List<KorisnikNalog>>() {}.getType());
    }

    public List<KorisnikNalog> fetchAll(){
        return (List<KorisnikNalog>) load(FILE_PATH);
    }

    public KorisnikNalog fetchByEmail(String ime) throws NotFoundException {
        List<KorisnikNalog> res = fetchAll();
        return res.stream().
                filter(kn -> kn.getKorisnik().getEmail().equals(ime)).
                findFirst().orElseThrow(NotFoundException::new);
    }
    public KorisnikNalog fetchById(String id) throws NotFoundException {
        List<KorisnikNalog> res = fetchAll();
        return res.stream().
                filter(kn -> kn.getKorisnik().getId().equals(id)).
                findFirst().orElseThrow(NotFoundException::new);
    }


    public KorisnikNalog create(KorisnikTrans korisnik) throws IOException {
        KorisnikNalog k = new KorisnikNalog(korisnik.getEmail(), korisnik.getIme(), korisnik.getPrezime(), korisnik.getOrganizacija(), Uloga.fromString(korisnik.getUloga()), korisnik.getSifra());
        k.getKorisnik().setId(UUID.randomUUID().toString());
        OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
        try {
            Organizacija o = organizacijaDAO.fetchById(k.getKorisnik().getOrganizacija());
            o.getKorisnici().add(k.getKorisnik().getId());
            organizacijaDAO.update(o,o.getId());
        }catch (NotFoundException nfe){
            throw new BadRequestException("Ne postoji organizacija sa datim id-jem");
        }
        List<KorisnikNalog> list = fetchAll();
        list.add(k);
        upisListeUFile(list);
        return k;
    }

    public KorisnikNalog update(KorisnikNalog noviKorisnikNalog) throws IOException {
        List<KorisnikNalog> korisnici = fetchAll();
        Korisnik korisnik = noviKorisnikNalog.getKorisnik();
        KorisnikNalog korisnikNalog = korisnici.stream().filter(kn-> kn.getKorisnik().getId().equals(korisnik.getId())).findFirst().orElseThrow(NotFoundException::new);
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


    public static boolean checkStringAttribute(String ime) {
        return ime!=null&& !ime.equals("");
    }

    public List<KorisnikNalog> delete(String id) throws IOException {
        OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
        Organizacija o = organizacijaDAO.fetchById(fetchById(id).getKorisnik().getOrganizacija());
        o.setKorisnici(o.getKorisnici().stream().filter(kid->!kid.equals(id)).collect(Collectors.toList()));
        organizacijaDAO.update(o, o.getId());
        List<KorisnikNalog> korisnici = fetchAll().stream()
                .filter((element) -> !element.getKorisnik().getId().equals(id))
                .collect(Collectors.toList());
        upisListeUFile(korisnici);
        return korisnici;
    }
    private void upisListeUFile(List<KorisnikNalog> korisnici) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(korisnici).getBytes());
    }
}
