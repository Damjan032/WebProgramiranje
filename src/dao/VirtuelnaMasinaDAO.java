package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import models.VirtuelnaMasina;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class VirtuelnaMasinaDAO {
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/virtuelneMasine.json";

    public List<VirtuelnaMasina> fetchAll() {
        try {
            JsonReader reader = new JsonReader(new FileReader(FILE_PATH));
            return g.fromJson(reader, new TypeToken<List<VirtuelnaMasina>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException("File " + FILE_PATH + " ne postoji.");
        }
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
        List<VirtuelnaMasina> list = fetchAll();
        virtuelnaMasina.setId(UUID.randomUUID().toString());
        list.add(virtuelnaMasina);
        upisListeUFile(list);
        return virtuelnaMasina;
    }

    public VirtuelnaMasina update(VirtuelnaMasina virtuelnaMasina, String id) throws IOException {
        List<VirtuelnaMasina> virtuelnaMasine= fetchAll();
        virtuelnaMasine.forEach(
                oldVM -> {
                    if (oldVM.getId().equals(id)) {
                        oldVM.setIme(virtuelnaMasina.getIme());
                        oldVM.setDiskovi(virtuelnaMasina.getDiskovi());
                        oldVM.setKategorija(virtuelnaMasina.getKategorija());
                        oldVM.setAktivnosti(virtuelnaMasina.getAktivnosti());
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
        Files.write(Paths.get(FILE_PATH), g.toJson(vmKategorije).getBytes());

    }
}
