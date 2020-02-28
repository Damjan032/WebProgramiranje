package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.OrganizacijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import exceptions.BadRequestException;
import exceptions.NotFoundException;
import exceptions.UnauthorizedException;
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
        List<Disk> diskovi = diskDAO.fetchAll();
        if(k.getUloga()== Uloga.SUPER_ADMIN){
            return diskovi.stream().map(this::mapToDiskDTOString).collect(Collectors.toList());
        }
        return diskovi.stream().filter(d->d.getOrganizacija().equals(k.getOrganizacija())).map(this::mapToDiskDTOString).collect(Collectors.toList());
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
        List<Resurs> resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi();
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
        Disk disk = g.fromJson(body, Disk.class);
        if (disk.getOrganizacija()==null){
            throw new BadRequestException("Niste uneli organizaciju!");
        }
        if (!disk.getOrganizacija().equals(k.getOrganizacija())&&k.getUloga()!=Uloga.SUPER_ADMIN){
            throw new BadRequestException("Ne mozete dodati disk drugoj organizaciji!");
        }
        if(disk.getIme()==null||disk.getTipDiska()==null) {
            throw new BadRequestException("Nisu uneti svi podaci!");
        }
        if (disk.getKapacitet()<=0){
            throw new BadRequestException("Kapacitet ne može biti negativan!");
        }
        disk = diskDAO.create(disk);
        return g.toJson(disk);
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
        Disk noviDisk = g.fromJson(body, Disk.class);
        if (noviDisk.getId()==null){
            throw new BadRequestException("Novi disk nema id starog pa se ne zna koji disk se menja!");
        }
        try {
            Disk stari = diskDAO.fetchById(noviDisk.getId());
            if (!stari.getOrganizacija().equals(k.getOrganizacija())&&k.getUloga()!=Uloga.SUPER_ADMIN) {
                throw new BadRequestException("Ne mozete da menjate disk druge organizacije!");
            }
        }catch (NotFoundException nfe){
            throw new BadRequestException("Disk kojeg zelite da menjate ne postoji!");
        }
        if (k.getUloga() == Uloga.SUPER_ADMIN) {
            return g.toJson(diskDAO.update(noviDisk, noviDisk.getId()));
        }
        List<String> resursiIDs = new OrganizacijaDAO().fetchById(k.getOrganizacija()).getResursi().stream().map(res->res.getId()).collect(Collectors.toList());
        if (!resursiIDs.contains(noviDisk.getId())) {
            throw new UnauthorizedException();
        }
        return g.toJson(diskDAO.update(noviDisk, noviDisk.getId()));
    }

    public List<String> delete(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        if (k==null){
            throw new UnauthorizedException();
        }
        String id = req.params("id");

        if (k.getUloga() != Uloga.SUPER_ADMIN) {
            throw new UnauthorizedException();
        }
        try{
            diskDAO.delete(id);
        }catch (NotFoundException nfe){
            throw new BadRequestException("Ne postoji disk sa tim id-jem");
        }
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
        Organizacija o = null;
        try{
            OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
            o = organizacijaDAO.fetchById(d.getOrganizacija());
        }catch (NotFoundException nfe){

        }
        return g.toJson(new DiskDTO.Builder().
                withId(d.getId()).
                withIme(d.getIme()).
                withKapacitet(d.getKapacitet()).
                withTip(d.getTipDiska()).
                withVm(vm).
                withOrg(o).
                build());
    }
}
