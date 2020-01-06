package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.KorisnikDAO;
import dao.OrganizacijaDAO;
import dto.DiskDTO;
import dto.KorisnikDTO;
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

import jdk.jshell.spi.ExecutionControl;
import models.Disk;
import models.Korisnik;
import models.KorisnikNalog;
import models.Organizacija;
import models.Organizacija.Resurs;
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
    public String create(String req) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Nema ovoga");
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

    public String updateWithImage(Request req, String id) throws IOException, ServletException {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        String ime = req.queryParams("oIme");
        String opis = req.queryParams("oOpis");
        if(ime.trim().equals("") || opis.trim().equals("")) {
            throw new BadRequestException("Sva polja moraju biti popunjena");
        }
        if (organizacijaDAO.fetchByIme(ime).isPresent() && !organizacijaDAO.fetchById(id).getIme().equalsIgnoreCase(ime)) {
            throw new BadRequestException("Organizacija sa imenom: " + ime +" posotji");
        }
        Organizacija organizacija = organizacijaDAO.fetchById(id);
        organizacija.setOpis(opis);
        organizacija.setIme(ime);
        organizacija.setImgPath(dodajSliku(req));

        return mapToOrganizacijaDTOString(organizacijaDAO.update(organizacija,id));
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
        List<KorisnikNalog> korisnici = new ArrayList<>();
        List<ResursDTO> resursi = new ArrayList<>();
        KorisnikDAO korisnikDAO = new KorisnikDAO();

        List<String> korisniciId = organizacija.getKorisnici();
        List<Resurs> resursiId = organizacija.getResursi();

        if (korisniciId != null) {
            organizacija.getKorisnici().forEach(korisnikId -> {
                // TODO Uz pomoc korinikDAO izvuci korinsike i upisi ih ovde za svaki id iz liste
                try {
                    korisnici.add(korisnikDAO.fetchByEmail(korisnikId));
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
        String path = "data/img/" + ime.toLowerCase().trim();

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.readAllBytes()));
        File outputfile = new File(path);
        ImageIO.write(img, type, outputfile);
        return path;
    }

    private ResursDTO mapToResursDTO(Resurs resurs) {
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
