package services;

import com.google.gson.Gson;
import dao.KorisnikDAO;
import dao.OrganizacijaDAO;
import dto.KorisnikDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import komunikacija.KorisnikTrans;
import models.Korisnik;
import models.KorisnikNalog;
import models.Organizacija;
import models.enums.Uloga;
import spark.Request;
import spark.Response;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class KorisnikService{


    private Gson g = new Gson();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();

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
        try {

                korisnikDAO.fetchByEmail(korisnikTrans.getEmail());
        }catch (NotFoundException nfe){
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
        throw new BadRequestException("Korisnik sa imenom"+korisnikTrans.getEmail()+" već postoji");
    }

    public String update(Request req, Response res) throws IOException {
        Korisnik korisnik = req.session().attribute("korisnik");
        if (korisnik == null){
            throw new UnauthorizedException();
        }
        String body = req.body();
        KorisnikNalog noviKorisnik = mapKorisnikTransToKorisnik(g.fromJson(body, KorisnikTrans.class));
        if (korisnikDAO.fetchByEmail(noviKorisnik.getKorisnik().getEmail()).getKorisnik().getUloga()== Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se menjati super admin!");
        }
        if (noviKorisnik.getKorisnik().getUloga()==Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne može se uloga promeniti na super admina!");
        }
        if(!korisnik.getEmail().equals(noviKorisnik.getKorisnik().getEmail())) {
            if(korisnik.getUloga()==Uloga.KORISNIK){
                if(!korisnik.getEmail().equals(noviKorisnik.getKorisnik().getEmail())){
                    throw new UnauthorizedException();
                }
            }
            if(korisnik.getUloga() == Uloga.ADMIN){
                if (noviKorisnik.getKorisnik().getUloga()!= Uloga.KORISNIK){
                    throw new BadRequestException("Možete menjati samo korisnike koji nisu admini!");
                }
                if(!korisnik.getOrganizacija().equals(noviKorisnik.getKorisnik().getOrganizacija())){
                    throw new UnauthorizedException();
                }
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


    private KorisnikNalog mapKorisnikTransToKorisnik(KorisnikTrans kt){
      //  return null;
        return new KorisnikNalog(kt.getEmail(), kt.getIme(), kt.getPrezime(), kt.getOrganizacija(), Uloga.fromString(kt.getUloga()), kt.getSifra());
    }


    private String mapToKorisnikDTOString(KorisnikNalog kn){

        Korisnik k = kn.getKorisnik();
        
        System.out.println(k.getUloga());
        return g.toJson(new KorisnikDTO.Builder().
                withIme(k.getIme()).
                withAktivnosti(k.getAktivnosti()).
                withEmail(k.getEmail()).
                withPrezime(k.getPrezime()).
                withOrganizacija(k.getOrganizacija()).
                withUloga(k.getUloga()).
                build());
    }
}
