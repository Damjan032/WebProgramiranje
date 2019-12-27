package services;

import com.google.gson.Gson;
import dao.KorisnikDAO;
import dto.KorisnikDTO;
import models.Aktivnost;
import models.Korisnik;
import models.Organizacija;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KorisnikService implements Service {


    private Gson g = new Gson();
    private KorisnikDAO korisnikDAO = new KorisnikDAO();

    @Override
    public List fetchAll() throws FileNotFoundException {

        return korisnikDAO.fetchAll();
    }

    @Override
    public Object fetchById(Object o) throws IOException {
        return korisnikDAO.fetchById((UUID) o);
    }

    @Override
    public Object create(String s) throws IOException {
        return null;
    }

    @Override
    public Object update(String body, Object o) throws IOException {
        return null;
    }

    @Override
    public void delete(String id) throws IOException {

    }

    private String mapToKorisnikDTOString(Korisnik korisnik) {
        List<Aktivnost> aktivnosti = new ArrayList<>();

        String organizacija = korisnik.getOrganizacija();

        Organizacija o =

        return
                g.toJson(new KorisnikDTO.Builder().w(organizacija.getId()).withIme(organizacija.getIme()).withImgPath(organizacija.getImgPath())
                        .withOpis(organizacija.getOpis()).withKorisnici(korisnici).withResursi(resursi).build());
    }
}
