package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.KorisnikDAO;
import dao.OrganizacijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import dto.KorisnikDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import komunikacija.DiskTrans;
import komunikacija.KorisnikTrans;
import models.*;
import models.enums.TipDiska;
import models.enums.TipResursa;
import models.enums.Uloga;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DiskService{
    private Gson g = new Gson();
    private DiskDAO diskDAO = new DiskDAO();


    public List<String> fetchAll(Request req) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        var diskovi = diskDAO.fetchAll();
        if(k.getUloga()== Uloga.SUPER_ADMIN){
            return diskovi.stream().map(this::mapToDiskDTOString).collect(Collectors.toList());
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        return diskovi.stream().filter(d->resursiIDs.contains(d.getId())).map(this::mapToDiskDTOString).collect(Collectors.toList());
    }

    public String fetchById(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        String id = req.params("id");
        Disk d = diskDAO.fetchById(id);
        if(k.getUloga()==Uloga.SUPER_ADMIN){
            return g.toJson(mapToDiskDTOString(d));
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        if (!resursiIDs.contains(d.getId())){
            throw new UnauthorizedException();
        }
        return g.toJson(mapToDiskDTOString(d));
    }

    public String create(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        if (k.getUloga()==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        String body = req.body();
        DiskTrans d = g.fromJson(body, DiskTrans.class);
        try{
            diskDAO.fetchById(d.getId());
        }catch (NotFoundException nfe){
            return g.toJson(diskDAO.create(mapDiskTransToDisk(d)));
        }
        throw new BadRequestException("Disk sa tim id-jem vec postoji!");
    }

    public String update(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        if (k.getUloga() == Uloga.KORISNIK) {
            throw new UnauthorizedException();
        }
        String body = req.body();
        DiskTrans d = g.fromJson(body, DiskTrans.class);
        Disk noviDisk = mapDiskTransToDisk(d);
        if (k.getUloga() == Uloga.SUPER_ADMIN) {
            return g.toJson(diskDAO.update(noviDisk, noviDisk.getId()));
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        if (!resursiIDs.contains(noviDisk.getId())) {
            throw new UnauthorizedException();
        }
        return g.toJson(diskDAO.update(noviDisk, noviDisk.getId()));
    }

    public List<String> delete(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        String id = req.params("id");

        if (k.getUloga() == Uloga.KORISNIK) {
            throw new UnauthorizedException();
        }
        if (k.getUloga() == Uloga.SUPER_ADMIN) {
            diskDAO.delete(id);
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        if (!resursiIDs.contains(id)){
            throw new UnauthorizedException();
        }
        return diskDAO.fetchAll().stream().map(this::mapToDiskDTOString).collect(Collectors.toList());
    }
    private Disk mapDiskTransToDisk(DiskTrans dt){
        //  return null;
        return new Disk(dt.getId(), dt.getIme(), TipDiska.fromString(dt.getTip()), dt.getKapacitet(), dt.getVm());
    }
    private String mapToDiskDTOString(Disk d){
        return g.toJson(new DiskDTO.Builder().
                withId(d.getId()).
                withIme(d.getIme()).
                withKapacitet(d.getKapacitet()).
                withTip(d.getTip()).
                withVm(d.getVm()).
                build());
    }
}
