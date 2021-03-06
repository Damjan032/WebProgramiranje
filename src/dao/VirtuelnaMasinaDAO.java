package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.BadRequestException;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import models.*;
import models.enums.TipResursa;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class VirtuelnaMasinaDAO extends Initializer{
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/virtuelneMasine.json";
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();

    public List<VirtuelnaMasina> fetchAll() {
        return (List<VirtuelnaMasina>) load(FILE_PATH);
    }

    public VirtuelnaMasina fetchById(String id) {
        return fetchAll().stream().filter(virtuelnaMasina -> virtuelnaMasina.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }

    public Optional<VirtuelnaMasina> fetchByKategorijaID(String kategorijaId) {
        return fetchAll().stream().filter(virtuelnaMasina -> virtuelnaMasina.getKategorija().equals(kategorijaId)).findFirst();
    }

    public Optional<VirtuelnaMasina> fetchByIme(String ime) {
        return fetchAll().stream().filter(virtuelnaMasina -> virtuelnaMasina.getIme().equals(ime)).findFirst();
    }

    public VirtuelnaMasina create(VirtuelnaMasina virtuelnaMasina) throws IOException {
        if (fetchByIme(virtuelnaMasina.getIme()).isPresent() ) {
            throw new BadRequestException("Kategorija VM sa imenom: " + virtuelnaMasina.getIme() +" postoji");
        }
        List<VirtuelnaMasina> list = fetchAll();
        virtuelnaMasina.setId(UUID.randomUUID().toString());
        try {
            Organizacija o = organizacijaDAO.fetchById(virtuelnaMasina.getOrganizacija());
            o.getResursi().add(new Resurs(virtuelnaMasina.getId(), TipResursa.VM));
            organizacijaDAO.update(o, o.getId());
        }catch (NotFoundException nfe) {
        }
        list.add(virtuelnaMasina);
        upisListeUFile(list);
        return virtuelnaMasina;
    }

    public VirtuelnaMasina update(VirtuelnaMasina virtuelnaMasina, String id) throws IOException {
    	Optional<VirtuelnaMasina> vm = fetchByIme(virtuelnaMasina.getIme());
        if (vm.isPresent() && !vm.get().getId().equals(id)){
            throw new BadRequestException("VM sa imenom: " + virtuelnaMasina.getIme() +" ne postoji");
        }
        List<VirtuelnaMasina> virtuelnaMasine= fetchAll();
        System.out.println("VM : "+virtuelnaMasine.size());

        virtuelnaMasine.forEach(
                oldVM -> {
                    if (oldVM.getId().equals(id)) {
                        if (KorisnikDAO.checkStringAttribute(virtuelnaMasina.getIme())) {
                            oldVM.setIme(virtuelnaMasina.getIme());
                        }
                        if (KorisnikDAO.checkStringAttribute(virtuelnaMasina.getKategorija())){
                            oldVM.setKategorija(virtuelnaMasina.getKategorija());
                        }
                        if (virtuelnaMasina.getAktivnosti()!=null){
                            oldVM.setAktivnosti(virtuelnaMasina.getAktivnosti());
                        }
                        if (virtuelnaMasina.getDiskovi()!=null){
                            oldVM.setDiskovi(virtuelnaMasina.getDiskovi());
                        }
                    }
                });

        upisListeUFile(virtuelnaMasine);
        return virtuelnaMasina;
    }

    public void delete(String id) throws IOException {
        upisListeUFile(
                fetchAll().stream()
                        .filter((element) -> !element.getId().equals(id))
                        .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<VirtuelnaMasina> vmKategorije) throws IOException {
        System.out.println("VM : "+vmKategorije.size());

        Files.write(Paths.get(FILE_PATH), g.toJson(vmKategorije).getBytes());

    }

    @Override
    protected List<Object> readData() throws FileNotFoundException {
        return g.fromJson(new JsonReader(new FileReader(FILE_PATH)),new TypeToken<List<VirtuelnaMasina>>() {}.getType());

    }
}
