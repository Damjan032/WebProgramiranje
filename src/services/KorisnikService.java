package services;

import com.google.gson.Gson;
import dao.KorisnikDAO;
import dao.OrganizacijaDAO;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class KorisnikService{


    private Gson g = new Gson();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();

    public List<String> fetchAll(Request req, Response res) throws FileNotFoundException {
        Korisnik korisnik = req.session().attribute("korisnik");
        if (korisnik == null){
            throw new UnauthorizedException();
        }
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
        if (k == null){
            throw new UnauthorizedException();
        }
        String email = req.params("email");
        if (k.getUloga() == Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        KorisnikNalog kn = korisnikDAO.fetchByEmail(email);
        if (kn==null){
            throw new NotFoundException();
        }
        Korisnik korisnik = kn.getKorisnik();
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
        if (k == null) {
            throw new UnauthorizedException();
        }
        String body = req.body();
        KorisnikTrans korisnikTrans = g.fromJson(body, KorisnikTrans.class);
        if (korisnikDAO.fetchByEmail(korisnikTrans.getEmail())!=null){
            throw new BadRequestException("Korisnik sa emailom "+korisnikTrans.getEmail()+" već postoji.");
        }
        if (Uloga.fromString(korisnikTrans.getUloga())==Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
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
        if (korisnik == null){
            throw new UnauthorizedException();
        }
        String body = req.body();
        Korisnik noviKorisnik = mapKorisnikTransToKorisnik(g.fromJson(body, KorisnikTrans.class));
        if (korisnikDAO.fetchByEmail(noviKorisnik.getEmail()).getKorisnik().getUloga()== Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se menjati super admin!");
        }
        if (noviKorisnik.getUloga()==Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se uloga promeniti na super admina!");
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
        return mapToKorisnikDTOString(korisnikDAO.update(noviKorisnik));
    }

    public List<String> delete(Request req, Response res) throws IOException {
        String email = req.params("email");
        Korisnik k = req.session().attribute("korisnik");
        if (k == null){
            throw new UnauthorizedException();
        }
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
        String organizacijaId = k.getOrganizacija();

        Organizacija org;
        Optional<Organizacija> optOrg;
        try {
            optOrg = organizacijaDAO.fetchByIme(organizacijaId);
            org = optOrg.get();
        } catch (Exception ignored) {
            org = null;
        }

        System.out.println(k.getUloga());
        return g.toJson(new KorisnikDTO.Builder().
                        withIme(k.getIme()).
                        withAktivnosti(k.getAktivnosti()).
                        withEmail(k.getEmail()).
                        withPrezime(k.getPrezime()).
                        withOrganizacija(org).
                        withUloga(k.getUloga()).
                        build());
    }
}
