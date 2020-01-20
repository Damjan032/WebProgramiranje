package services;

import com.google.gson.Gson;
import dao.*;
import dto.*;
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

import exceptions.NotFoundException;
import models.*;
import models.enums.TipResursa;
import spark.Request;

public class OrganizacijaService implements Service<String, String> {

    private Gson g = new Gson();
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
    private DiskDAO diskDAO = new DiskDAO();
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO = new VirtuelnaMasinaDAO();
    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        return organizacijaDAO.fetchAll().stream().map(this::mapToOrganizacijaDTOString).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String id) throws FileNotFoundException {
        return mapToOrganizacijaDTOString(organizacijaDAO.fetchById(id));
    }

    @Override
    public String create(String s) throws IOException {
        return null;
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
        List<ResursDTO> resursiDTO = new ArrayList<>();
        KorisnikDAO korisnikDAO = new KorisnikDAO();

        List<String> korisniciId = organizacija.getKorisnici();
        List<Resurs> resursiId = organizacija.getResursi();
        var resursi = new ArrayList<Resurs>();
        resursiId.forEach(resurs->{
            String id = resurs.getId();
            if(resurs.getTip()==TipResursa.DISK){
                resursi.add(diskDAO.fetchById(id));
            }else{
                resursi.add(virtuelnaMasinaDAO.fetchById(id));
            }
        });
        if (korisniciId != null) {
            organizacija.getKorisnici().forEach(korisnikId -> {
                KorisnikNalog k = null;
                try {
                    k = korisnikDAO.fetchByEmail(korisnikId);
                }catch (NotFoundException nfe) {
                    nfe.printStackTrace();
                }
                korisnici.add(k);

            });
        }

        if (resursiId != null) {
            organizacija.getResursi().forEach(resurs -> {
                resursiDTO.add(mapToResursDTO(resurs));
            });
        }
        return
                g.toJson(new OrganizacijaDTO.Builder().withId(organizacija.getId()).withIme(organizacija.getIme())
                        .withImgPath(organizacija.getImgPath())
                        .withOpis(organizacija.getOpis()).withKorisnici(korisnici).withResursi(resursiDTO).build());
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
                Disk disk = diskDAO.fetchById(resurs.getId());
                return new DiskDTO.Builder().withId(disk.getId()).withIme(disk.getIme()).withKapacitet(disk.getKapacitet())
                        .withTip(disk.getTipDiska()).withTipResursa(resurs.getTip()).withVm(disk.getVm()).build();
          case VM:
               //public VirtuelnaMasinaDTO(String id, String ime, VMKategorija kategorija, List<Disk> diskovi, List<Aktivnost> aktivnosti)
              VirtuelnaMasina virtuelnaMasina = virtuelnaMasinaDAO.fetchById(resurs.getId());
              List<Disk> diskovi = new ArrayList<>();
              virtuelnaMasina.getDiskovi().forEach(diskId ->{
                  diskovi.add(diskDAO.fetchById(diskId));
              });
              VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();
              return new VirtuelnaMasinaDTO(virtuelnaMasina.getId(), virtuelnaMasina.getIme(), vmKategorijaDAO.fetchById(virtuelnaMasina.getKategorija()), diskovi, virtuelnaMasina.getAktivnosti());
            default:
                return null;
        }
    }

    public boolean addVM(String req, String id, String vmID) throws IOException {
        Organizacija organizacija = organizacijaDAO.fetchById(id);

        if(organizacija.getResursi().stream().filter(resurs -> resurs.getId().equals(vmID)).findFirst().isPresent()){
            throw new BadRequestException("Izaberi virtuelna masina je vec u listi resursa date organizacije.");
        }
        organizacija.getResursi().add(new Resurs(vmID, TipResursa.VM));
        update(g.toJson(organizacija), id);
        return true;
    }
}
