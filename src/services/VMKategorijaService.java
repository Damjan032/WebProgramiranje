package services;

import com.google.gson.Gson;
import dao.OrganizacijaDAO;
import dao.VMKategorijaDAO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import models.Korisnik;
import models.Organizacija;
import models.VMKategorija;
import models.VirtuelnaMasina;
import models.enums.Uloga;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class VMKategorijaService implements Service<String, String> {
    private Gson g = new Gson();
    private VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();


    @Override
    public List<String> fetchAll(Request req) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        var vm = vmKategorijaDAO.fetchAll();
        if (u != Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }

        return vm.stream().map(vmKategorija -> g.toJson(vmKategorija, VMKategorija.class)).collect(Collectors.toList());
    }

    @Override
    public String fetchById(Request req, String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        if (u != Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }
        return g.toJson(vmKategorijaDAO.fetchById(id));
    }

    @Override
    public String create(Request req) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        var vm = vmKategorijaDAO.fetchAll();
        if (u != Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }
        VMKategorija vmKategorija = g.fromJson(req.body(), VMKategorija.class);
        if (vmKategorijaDAO.fetchByIme(vmKategorija.getIme()).isPresent() ) {
            throw new BadRequestException("Kategorija VM sa imenom: " + vmKategorija.getIme() +" posotji");
        }
        return g.toJson(vmKategorijaDAO.create(vmKategorija));
    }

    @Override
    public String update(Request req, String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        if (u != Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }
        VMKategorija vmKategorija = g.fromJson(req.body(), VMKategorija.class);
        String ime = vmKategorija.getIme();
        if (vmKategorijaDAO.fetchByIme(vmKategorija.getIme()).isPresent() && !vmKategorijaDAO.fetchById(id).getIme().equalsIgnoreCase(ime)) {
            throw new BadRequestException("Kategorija VM sa imenom: " + vmKategorija.getIme() +" posotji");
        }
        return g.toJson(vmKategorijaDAO.update(vmKategorija, id));
    }

    @Override
    public void delete(Request req, String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        var vm = vmKategorijaDAO.fetchAll();
        if (u != Uloga.SUPER_ADMIN){
            throw new UnauthorizedException();
        }
        VirtuelnaMasinaService virtuelnaMasinaService = new VirtuelnaMasinaService();
        if(virtuelnaMasinaService.fetchByKatgegorijaId(id)){
            throw new BadRequestException("Kategorija VM ne sme biti obrisana jer postoje VM te kategorije");
        }
        vmKategorijaDAO.delete(id);
    }
}
