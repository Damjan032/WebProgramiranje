package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.OrganizacijaDAO;
import dto.DiskDTO;
import dto.OrganizacijaDTO;
import dto.ResursDTO;
import exceptions.BadRequestException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import models.Disk;
import models.Korisnik;
import models.Organizacija;
import models.enums.Uloga;
import spark.Request;

public class OrganizacijaService implements Service<String, String> {

    private Gson g = new Gson();
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
    private DiskDAO diskDTO = new DiskDAO();

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
            throw new BadRequestException("Organizacija sa imenom: " + ime +" posotji");
        }
        Organizacija organizacija = new Organizacija(null, ime, opis, dodajSliku(req), new ArrayList<>(), new ArrayList<>());
        return mapToOrganizacijaDTOString(organizacijaDAO.create(organizacija));
    }

    @Override
    public String update(String organizacija, String id) throws IOException {
        return mapToOrganizacijaDTOString(organizacijaDAO.update(g.fromJson(organizacija, Organizacija.class), id));
    }

    @Override
    public void delete(String id) throws IOException {
        organizacijaDAO.delete(id);
    }

    private String mapToOrganizacijaDTOString(Organizacija organizacija) {
        List<Korisnik> korisnici = new ArrayList<>();
        List<ResursDTO> resursi = new ArrayList<>();

        List<String> korisniciId = organizacija.getKorisnici();
        List<Organizacija.Resurs> resursiId = organizacija.getResursi();

        if (korisniciId != null) {
            organizacija.getKorisnici().forEach(korisnikId -> {
                // TODO Uz pomoc korinikDAO izvuci korinsike i upisi ih ovde za svaki id iz liste
                korisnici.add(new Korisnik(Uloga.ADMIN));
            });
        }

        if (resursiId != null) {
            organizacija.getResursi().forEach(resurs -> {
                resursi.add(mapToResursDTO(resurs));
            });
        }
        return
            g.toJson(new OrganizacijaDTO.Builder().withId(organizacija.getId()).withIme(organizacija.getIme())
                .withImgPath(organizacija.getImgPath())
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

    private ResursDTO mapToResursDTO(Organizacija.Resurs resurs) {
        switch (resurs.getTip()) {
            case DISK:
                Disk disk = diskDTO.fetchById(resurs.getId());
                return new DiskDTO.Builder().withId(disk.getId()).withIme(disk.getIme()).withKapacitet(disk.getKapacitet())
                    .withTip(disk.getTip()).withTipResursa(resurs.getTip()).withVm(disk.getVm()).build();
//          case VM:
//              resursi.add(vmDTO.fetch(resurs.getId()));
//          break;
            default:
                return null;
        }
    }
}
