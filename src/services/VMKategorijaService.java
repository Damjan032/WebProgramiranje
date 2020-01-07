package services;

import com.google.gson.Gson;
import dao.VMKategorijaDAO;
import exceptions.BadRequestException;
import models.VMKategorija;
import models.VirtuelnaMasina;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VMKategorijaService implements Service<String, String> {
    private Gson g = new Gson();
    private VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();


    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        return vmKategorijaDAO.fetchAll().stream().map(vmKategorija -> g.toJson(vmKategorija, VMKategorija.class)).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String id) throws IOException {
        return g.toJson(vmKategorijaDAO.fetchById(id));
    }

    @Override
    public String create(String s) throws IOException {
        VMKategorija vmKategorija = g.fromJson(s, VMKategorija.class);
        if (vmKategorijaDAO.fetchByIme(vmKategorija.getIme()).isPresent() ) {
            throw new BadRequestException("Kategorija VM sa imenom: " + vmKategorija.getIme() +" posotji");
        }
        return g.toJson(vmKategorijaDAO.create(vmKategorija));
    }

    @Override
    public String update(String body, String id) throws IOException {
        VMKategorija vmKategorija = g.fromJson(body, VMKategorija.class);
        String ime = vmKategorija.getIme();
        if (vmKategorijaDAO.fetchByIme(vmKategorija.getIme()).isPresent() && !vmKategorijaDAO.fetchById(id).getIme().equalsIgnoreCase(ime)) {
            throw new BadRequestException("Kategorija VM sa imenom: " + vmKategorija.getIme() +" posotji");
        }
        return g.toJson(vmKategorijaDAO.update(vmKategorija, id));
    }

    @Override
    public void delete(String id) throws IOException {
        VirtuelnaMasinaService virtuelnaMasinaService = new VirtuelnaMasinaService();
        if(virtuelnaMasinaService.fetchByKatgegorijaId(id)){
            throw new BadRequestException("Kategorija VM ne sme biti obrisana jer postoje VM te kategorije");
        }
        vmKategorijaDAO.delete(id);
    }
}
