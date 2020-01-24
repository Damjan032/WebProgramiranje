package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.OrganizacijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
import komunikacija.DiskTrans;
import models.*;
import models.enums.TipResursa;
import models.enums.Uloga;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DiskService{
    private Gson g = new Gson();
    private DiskDAO diskDAO = new DiskDAO();
    private VirtuelnaMasinaDAO vmDAO = new VirtuelnaMasinaDAO();


    public List<String> fetchAll(Request req) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        var diskovi = diskDAO.fetchAll();
        if(k.getUloga()== Uloga.SUPER_ADMIN){
            return diskovi.stream().map(this::mapToDiskDTOString).collect(Collectors.toList());
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        return diskovi.stream().filter(d->resursiIDs.contains(d.getId())).map(this::mapToDiskDTOString).collect(Collectors.toList());
    }

    public String fetchById(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
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
        if (k==null){
            throw new UnauthorizedException();
        }
        if (k.getUloga()==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }
        String body = req.body();
        DiskTrans diskTrans = g.fromJson(body, DiskTrans.class);
        Disk disk = diskTrans.getDisk();
        if(disk.getIme()==null||disk.getTipDiska()==null) {
            throw new BadRequestException("Nisu uneti svi podaci!");
        }
        if (disk.getKapacitet()<=0){
            throw new BadRequestException("Kapacitet ne može biti negativan!");
        }
        try{
            diskDAO.fetchByName(disk.getIme());
        } catch (NotFoundException nfe){
            OrganizacijaDAO odao = new OrganizacijaDAO();
            Organizacija o = odao.fetchById(diskTrans.getOrg());
            disk = diskDAO.create(disk);
            o.getResursi().add(new Resurs(disk.getId(), TipResursa.DISK));
            odao.update(o, o.getId());
            return g.toJson(disk);
        } catch (Exception e){
            e.printStackTrace();
        }
        throw new BadRequestException("Disk sa tim imenom vec postoji!");
    }



    public String update(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        if (k.getUloga() == Uloga.KORISNIK) {
            throw new UnauthorizedException();
        }
        String body = req.body();
        DiskDTO d = g.fromJson(body, DiskDTO.class);
        Disk noviDisk = mapDiskDTOToDisk(d);
        if (k.getUloga() == Uloga.SUPER_ADMIN) {
            return g.toJson(diskDAO.update(noviDisk, d.getId()));
        }
        var resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
        if (!resursiIDs.contains(noviDisk.getId())) {
            throw new UnauthorizedException();
        }
        return g.toJson(diskDAO.update(noviDisk, d.getId()));
    }

    public List<String> delete(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        String id = req.params("id");

        if (k.getUloga() != Uloga.SUPER_ADMIN) {
            throw new UnauthorizedException();
        }
        diskDAO.delete(id);
        return diskDAO.fetchAll().stream().map(this::mapToDiskDTOString).collect(Collectors.toList());
    }
    private Disk mapDiskDTOToDisk(DiskDTO dt){
        //  return null;
        String id=null;
        if (dt.getVm()!=null){
            id = dt.getVm().getId();
        }
        return new Disk(dt.getId(), dt.getIme(), dt.getTip(), dt.getKapacitet(), id);
    }
    private String mapToDiskDTOString(Disk d){
        VirtuelnaMasina vm = null;
        if (d.getVm()!=null){
            vm = vmDAO.fetchById(d.getVm());
        }
        return g.toJson(new DiskDTO.Builder().
                withId(d.getId()).
                withIme(d.getIme()).
                withKapacitet(d.getKapacitet()).
                withTip(d.getTipDiska()).
                withVm(vm).
                build());
    }
}
