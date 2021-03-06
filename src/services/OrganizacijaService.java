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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import models.*;
import models.enums.TipResursa;
import models.enums.Uloga;
import spark.Request;

public class OrganizacijaService implements Service<String, String> {

    private Gson g = new Gson();
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
    private DiskDAO diskDAO = new DiskDAO();
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO = new VirtuelnaMasinaDAO();
    @Override
    public List<String> fetchAll(Request req) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        if (k.getUloga()!=Uloga.SUPER_ADMIN){
            return organizacijaDAO.fetchAll().stream().filter(org->org.getKorisnici().contains(k.getId())).map(this::mapToOrganizacijaDTOString).collect(Collectors.toList());
        }
        return organizacijaDAO.fetchAll().stream().map(this::mapToOrganizacijaDTOString).collect(Collectors.toList());
    }

    @Override
    public String fetchById(Request req, String id) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        if (k.getUloga()!=Uloga.SUPER_ADMIN){
            if (!k.getOrganizacija().equals(id)){
                throw new UnauthorizedException();
            }
        }
        return mapToOrganizacijaDTOString(organizacijaDAO.fetchById(id));
    }

    @Override
    public String create(Request req) {

        Korisnik k = req.session().attribute("korisnik");
        if (k==null||k.getUloga() != Uloga.SUPER_ADMIN) {
            throw new UnauthorizedException();
        }
        return null;
    }

    public String createWithImage(Request req) throws IOException, ServletException {
        create(req);
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        String ime = req.queryParams("oIme");
        String opis = req.queryParams("oOpis");

        if (organizacijaDAO.fetchByIme(ime).isPresent()) {
            throw new BadRequestException("Organizacija sa imenom: " + ime +" posotji");
        }
        String slika = dodajSliku(req);
        if (!KorisnikDAO.checkStringAttribute(slika)){
            throw new BadRequestException("Niste dodalo logo za organizaciju!");
        }

        Organizacija organizacija = new Organizacija(null, ime, opis, slika, new ArrayList<>(), new ArrayList<>());
        return mapToOrganizacijaDTOString(organizacijaDAO.create(organizacija));
    }

    public String updateWithImage(Request req, String id) throws IOException, ServletException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        Uloga u = k.getUloga();
        if (u==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }else if (u== Uloga.ADMIN){
            if (!organizacijaDAO.fetchById(id).getKorisnici().contains(k.getId())){
                throw new UnauthorizedException();
            }
        }
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        String ime = req.queryParams("oIme");
        String opis = req.queryParams("oOpis");
        if(ime.trim().equals("") || opis.trim().equals("")) {
            throw new BadRequestException("Sva polja moraju biti popunjena");
        }
        if (organizacijaDAO.fetchByIme(ime).isPresent() && !organizacijaDAO.fetchById(id).getIme().equalsIgnoreCase(ime)) {
            throw new BadRequestException("Organizacija sa imenom: " + ime +" postoji");
        }
        Organizacija organizacija = organizacijaDAO.fetchById(id);
        organizacija.setOpis(opis);
        organizacija.setIme(ime);
        organizacija.setImgPath(dodajSliku(req));

        return mapToOrganizacijaDTOString(organizacijaDAO.update(organizacija,id));
    }

    @Override
    public String update(Request req, String s) throws IOException {
        return null;
    }

    public String update(String organizacija, String id) throws IOException {
        return mapToOrganizacijaDTOString(organizacijaDAO.update(g.fromJson(organizacija, Organizacija.class), id));
    }

    @Override
    public void delete(Request r, String id) throws IOException {
        Korisnik k = r.session().attribute("korisnik");
        if (k==null||k.getUloga()!=Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }
        organizacijaDAO.delete(id);
    }

    private String mapToOrganizacijaDTOString(Organizacija organizacija) {
        List<Korisnik> korisnici = new ArrayList<>();
        List<ResursDTO> resursiDTO = new ArrayList<>();
        KorisnikDAO korisnikDAO = new KorisnikDAO();

        List<String> korisniciId = organizacija.getKorisnici();
        List<Resurs> resursiId = organizacija.getResursi();
        if (korisniciId != null) {
            organizacija.getKorisnici().forEach(korisnikId -> {
                KorisnikNalog k = null;
                try {
                    k = korisnikDAO.fetchById(korisnikId);
                    korisnici.add(k.getKorisnik());
                }catch (NotFoundException nfe) {
                }

            });
        }

        if (resursiId != null) {
           for(var resurs:resursiId){
                ResursDTO res = mapToResursDTO(resurs);
                if (res!=null) {
                    resursiDTO.add(res);
                }
            }
        }
        return
                g.toJson(new OrganizacijaDTO.Builder().withId(organizacija.getId()).withIme(organizacija.getIme())
                        .withImgPath(organizacija.getImgPath())
                        .withOpis(organizacija.getOpis()).withKorisnici(korisnici).withResursi(resursiDTO).build());
    }

    private String dodajSliku(Request req) throws IOException, ServletException {
        String ime = req.queryParams("nazivSlike");
        if(!KorisnikDAO.checkStringAttribute(ime)){
            return null;
        }
        String type = ime.split("\\.")[1];
        InputStream file = req.raw().getPart("oSlika").getInputStream();
        String[] types = {"png", "jpg", "gif"};

        if (!Arrays.stream(types).anyMatch(type::equals)) {
            throw new BadRequestException("Molimo Vas izaberiti sliku (jpg, png, gif).");
        }
        String path = "data/img/" + ime.toLowerCase().trim();

        byte[] bytes = new byte[file.available()];
        file.read(bytes);
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
        File outputfile = new File(path);
        ImageIO.write(img, type, outputfile);
        return path;
    }

    private ResursDTO mapToResursDTO(Resurs resurs) {
        switch (resurs.getTip()) {
            case DISK:
                Disk disk;
                try {
                    disk = diskDAO.fetchById(resurs.getId());
                }catch (NotFoundException nfe){
                    return null;
                }
                VirtuelnaMasina vm = null;
                if(KorisnikDAO.checkStringAttribute(disk.getVm())) {
                    try {

                        vm = virtuelnaMasinaDAO.fetchById(disk.getVm());
                    }
                    catch (NotFoundException e){

                    }
                }
                return new DiskDTO.Builder().withId(disk.getId()).withIme(disk.getIme()).withKapacitet(disk.getKapacitet())
                        .withTip(disk.getTipDiska()).withTipResursa(resurs.getTip()).withVm(vm).build();
          case VM:
               //public VirtuelnaMasinaDTO(String id, String ime, VMKategorija kategorija, List<Disk> diskovi, List<Aktivnost> aktivnosti)
              VirtuelnaMasina virtuelnaMasina;
              try {
                  virtuelnaMasina = virtuelnaMasinaDAO.fetchById(resurs.getId());
              }catch (NotFoundException nfe){
                  return null;
              }
              Organizacija o;
              try {
                    o = organizacijaDAO.fetchById(virtuelnaMasina.getOrganizacija());
              }catch (NotFoundException nfe){
                  return null;
              }
              List<Disk> diskovi = new ArrayList<>();
              if (virtuelnaMasina.getDiskovi()!=null) {
                  virtuelnaMasina.getDiskovi().forEach(diskId -> {
                      try {
                          diskovi.add(diskDAO.fetchById(diskId));
                      }catch (NotFoundException ignored){

                      }
                  });
              }
              VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();
              return new VirtuelnaMasinaDTO(virtuelnaMasina.getId(), virtuelnaMasina.getIme(), vmKategorijaDAO.fetchById(virtuelnaMasina.getKategorija()), diskovi, virtuelnaMasina.getAktivnosti(), o);
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
