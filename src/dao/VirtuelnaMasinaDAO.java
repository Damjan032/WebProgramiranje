package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import models.VirtualMachine;

public class VirtuelnaMasinaDAO {

    private Gson g = new Gson();
    private static String FILE_PATH = "./data/vm.json";

    public List<VirtualMachine> fetchAll() {
        try {
            JsonReader reader = new JsonReader(new FileReader(FILE_PATH));
            return g.fromJson(reader, new TypeToken<List<VirtualMachine>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException("File " + FILE_PATH + " ne postoji.");
        }
    }

    public VirtualMachine fetchById(String id) {
        return fetchAll().stream().filter(virtuelnaMasina -> virtuelnaMasina.getId().equals(id)).findFirst()
            .orElseThrow(NotFoundException::new);
    }



    public Optional<VirtualMachine> fetchByIme(String ime) {
        return fetchAll().stream().filter(virtuelnaMasina -> virtuelnaMasina.getIme().equals(ime)).findFirst();
    }

    public VirtualMachine create(VirtualMachine virtuelnaMasina) throws IOException {
        List<VirtualMachine> list = fetchAll();
        virtuelnaMasina.setId(UUID.randomUUID().toString());
        list.add(virtuelnaMasina);
        upisListeUFile(list);
        return virtuelnaMasina;
    }

    public VirtualMachine update(VirtualMachine virtuelnaMasina, String id) throws IOException {
        List<VirtualMachine> virtuelneMasine = fetchAll();
        virtuelneMasine.forEach(
            oldVM -> {
                if (oldVM.getId().equals(id)) {
                    oldVM.setIme(virtuelnaMasina.getIme());
                    oldVM.setDiskovi(virtuelnaMasina.getDiskovi());
                    oldVM.setKategorija(virtuelnaMasina.getKategorija());
                }
            });

        upisListeUFile(virtuelneMasine);
        return virtuelnaMasina;
    }

    public void delete(String id) throws IOException {
        upisListeUFile(
            fetchAll().stream()
                .filter((element) -> !element.getId().equals(id))
                .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<VirtualMachine> virtuelneMasine) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(virtuelneMasine).getBytes());
    }


}
