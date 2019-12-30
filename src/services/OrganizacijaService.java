package services;

import com.google.gson.Gson;
import dao.OrganizacijaDAO;
import dto.OrganizacijaDTO;
import exceptions.BadRequestException;
import models.Korisnik;
import models.Organizacija;
import models.Resurs;
import models.enums.Uloga;
import spark.Request;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrganizacijaService implements Service<String, String> {
    private Gson g = new Gson();
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();

    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        return organizacijaDAO.fetchAll().stream().map(this::mapToOrganizacijaDTOString).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String id) throws FileNotFoundException {
        return mapToOrganizacijaDTOString(organizacijaDAO.fetchById(id));
    }

    @Override
    public String create(String req) {
        return "";
    }

    public String createWithImage(Request req) throws IOException, ServletException {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        String ime = req.queryParams("oIme");
        String opis = req.queryParams("oOpis");

        if (organizacijaDAO.fetchByIme(ime).isPresent()) {
            throw new BadRequestException("Organizacija sa tim imenom posotji");
        }
        Organizacija organizacija = new Organizacija(null, ime, opis, dodajSliku(req), new ArrayList<>(), new ArrayList<>());
        return mapToOrganizacijaDTOString(organizacijaDAO.create(organizacija));
    }

    @Override
    public String update(String organizacija, String id) throws IOException {
        return mapToOrganizacijaDTOString(organizacijaDAO.update(g.fromJson(organizacija, Organizacija.class), id));
    }

    @Override
    public List<String> delete(String id) throws IOException {
       return  organizacijaDAO.delete(id).stream().map(this::mapToOrganizacijaDTOString).collect(Collectors.toList());
    }

    private String mapToOrganizacijaDTOString(Organizacija organizacija) {
        List<Korisnik> korisnici = new ArrayList<>();
        List<Resurs> resursi = new ArrayList<>();

        List<String> korisniciId = organizacija.getKorisnici();
        List<String> resursiId = organizacija.getResursi();

        if (korisniciId != null) {



            organizacija.getKorisnici().forEach(korisnikId -> {
                // TODO Uz pomoc korinikDAO izvuci korinsike i upisi ih ovde za svaki id iz liste
                korisnici.add(new Korisnik(Uloga.ADMIN));
            });
        }
        if (resursiId != null) {
            organizacija.getResursi().forEach(resurs -> {
                // TODO Isto koa gore, samo sto ovde imate problem resurs ce morati da ima tip i id koji cete cuvati u jsonu i onda:

//            switch (resurs.getTip()) {
//                case DISK:
//                    resursi.add(diskDTO.fetch(resurs.getId()));
//                    break;
//                case VM:
//                    resursi.add(vmDTO.fetch(resurs.getId()));
//                    break;
//            }
            });
        }
        return
                g.toJson(new OrganizacijaDTO.Builder().withId(organizacija.getId()).withIme(organizacija.getIme()).withImgPath(organizacija.getImgPath())
                        .withOpis(organizacija.getOpis()).withKorisnici(korisnici).withResursi(resursi).build());
    }

    private String dodajSliku(Request req) throws IOException, ServletException {
        String type = req.queryParams("nazivSlike").split("\\.")[1];
        String ime = req.queryParams("nazivSlike");
        InputStream file = req.raw().getPart("oSlika").getInputStream();
        String[] types = {"png", "jpg", "gif"};

        if (!Arrays.stream(types).anyMatch(type::equals)) {
            throw new BadRequestException("Molimo Vas izaberiti sliku (jpg, png, gif).");
        }
        String path = "data/img/" + ime.toLowerCase().trim() + "." + type;

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.readAllBytes()));
        File outputfile = new File(path);
        ImageIO.write(img, type, outputfile);
        return path;
    }
}
