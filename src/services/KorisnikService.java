package services;

import com.google.gson.Gson;
import dao.KorisnikDAO;
import dto.KorisnikDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import komunikacija.KorisnikTrans;
import models.Aktivnost;
import models.Korisnik;
import models.KorisnikNalog;
import models.Organizacija;
import models.enums.Uloga;
import spark.Request;
import spark.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class KorisnikService{


    private Gson g = new Gson();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();

    public List<String> fetchAll(Request req, Response res) throws FileNotFoundException {
        Korisnik korisnik = req.session().attribute("korisnik");
        if(korisnik.getUloga() == Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        var korisnici = korisnikDAO.fetchAll().stream().filter(k->k.getKorisnik().getUloga()!=Uloga.SUPER_ADMIN).collect(Collectors.toList());
        if(korisnik.getUloga() == Uloga.ADMIN){
            korisnici = korisnici.stream().filter(korisnikNalog -> korisnikNalog.getKorisnik().getOrganizacija().equals(korisnik.getOrganizacija())).collect(Collectors.toList());
        }
        return korisnici.stream().map(this::mapToKorisnikDTOString).collect(Collectors.toList());
    }

    public String fetchById(Request req, Response res) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        String email = req.params("email");
        if (k.getUloga() == Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        Korisnik korisnik = korisnikDAO.fetchByEmail(email).getKorisnik();
        if (korisnik==null){
            throw new NotFoundException();
        }
        if (k.getUloga() == Uloga.ADMIN && !korisnik.getOrganizacija().equals(k.getOrganizacija())) {
            throw new UnauthorizedException();
        }
        KorisnikNalog k1 =korisnikDAO.fetchByEmail(email);
        if (k1 == null){
            throw new NotFoundException();
        }
        return mapToKorisnikDTOString(k1);
    }

    public String create(Request req, Response res) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        String body = req.body();
        KorisnikTrans korisnikTrans = g.fromJson(body, KorisnikTrans.class);
        if (Uloga.fromString(korisnikTrans.getUloga())==Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ova akcija je nedozvoljena.");
        }
        if (k.getUloga() == Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        if (k.getUloga() == Uloga.ADMIN && !k.getOrganizacija().equals(korisnikTrans.getOrganizacija())){
            throw new BadRequestException("Uneli ste organizaciju koja ne odgovara vašoj.");
        }
        KorisnikNalog newKorisnik = korisnikDAO.create(korisnikTrans);
        return mapToKorisnikDTOString(newKorisnik);
    }

    public String update(Request req, Response res) throws IOException {
        Korisnik korisnik = req.session().attribute("korisnik");
        String body = req.body();
        String email = req.params("email");
        Korisnik noviKorisnik = mapKorisnikTransToKorisnik(g.fromJson(body, KorisnikTrans.class));
        if (noviKorisnik.getUloga()==Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se menjati super admin!");
        }
        if(korisnik.getUloga()==Uloga.KORISNIK){
            if(!korisnik.getEmail().equals(noviKorisnik.getEmail())){
                throw new UnauthorizedException();
            }
        }
        if(korisnik.getUloga() == Uloga.ADMIN){
            if (noviKorisnik.getUloga()!= Uloga.KORISNIK){
                throw new BadRequestException("Možete menjati samo korisnike koji nisu admini!");
            }
            if(!korisnik.getOrganizacija().equals(noviKorisnik.getOrganizacija())){
                throw new UnauthorizedException();
            }
        }
        return mapToKorisnikDTOString(korisnikDAO.update(noviKorisnik,email));
    }

    public List<String> delete(Request req, Response res) throws IOException {
        String email = req.params("email");
        Korisnik k = req.session().attribute("korisnik");
        Korisnik korisnikBrisani = korisnikDAO.fetchByEmail(email).getKorisnik();
        if (korisnikBrisani.getUloga()==Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se brisati super admin!");
        }
        if (k.getUloga() == Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        if (k.getUloga() == Uloga.ADMIN && !k.getOrganizacija().equals(korisnikBrisani.getOrganizacija())){
            throw new UnauthorizedException();
        }
        return korisnikDAO.delete(email).stream().map(this::mapToKorisnikDTOString).collect(Collectors.toList());
    }


    private Korisnik mapKorisnikTransToKorisnik(KorisnikTrans kt){
        return new Korisnik(kt.getEmail(), kt.getIme(), kt.getPrezime(), kt.getOrganizacija(), Uloga.fromString(kt.getUloga()));
    }


    private String mapToKorisnikDTOString(KorisnikNalog kn) {

        Korisnik k = kn.getKorisnik();
        String organizacija = k.getOrganizacija();



        Organizacija o = null;


        return g.toJson(new KorisnikDTO.Builder().
                        withIme(k.getIme()).
                        withAktivnosti(k.getAktivnosti()).
                        withEmail(k.getEmail()).
                        withPrezime(k.getPrezime()).
                        withOrganizacija(o).
                        build());
    }
}
