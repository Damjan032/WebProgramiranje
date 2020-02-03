package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;
import models.Disk;
import models.KorisnikNalog;
import models.VMKategorija;
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

public class VMKategorijaDAO extends Initializer{
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/vmKategorija.json";

    public List<VMKategorija> fetchAll() {
        return (List<VMKategorija>) load(FILE_PATH);
    }

    public VMKategorija fetchById(String id) {
        return fetchAll().stream().filter(vmKategorija -> vmKategorija.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }

    public Optional<VMKategorija> fetchByIme(String ime) {
        return fetchAll().stream().filter(vmKategorija -> vmKategorija.getIme().equals(ime)).findFirst();
    }

    public VMKategorija create(VMKategorija vmKategorija) throws IOException {
        List<VMKategorija> list = fetchAll();
        vmKategorija.setId(UUID.randomUUID().toString());
        list.add(vmKategorija);
        upisListeUFile(list);
        return vmKategorija;
    }

    public VMKategorija update(VMKategorija vmKategorija, String id) throws IOException {
        List<VMKategorija> vmKategorije = fetchAll();
        vmKategorije.forEach(
                oldVMkat -> {
                    if (oldVMkat.getId().equals(id)) {
                        if (KorisnikDAO.checkStringAttribute(vmKategorija.getIme())) {
                            oldVMkat.setIme(vmKategorija.getIme());
                        }
                        if (vmKategorija.getBrGPU()>0) {
                            oldVMkat.setBrGPU(vmKategorija.getBrGPU());
                        }
                        if (vmKategorija.getBrJezgra()>0) {
                            oldVMkat.setBrJezgra(vmKategorija.getBrJezgra());
                        }
                        if (vmKategorija.getRAM()>0) {
                            oldVMkat.setRAM(vmKategorija.getRAM());
                        }
                    }
                });

        upisListeUFile(vmKategorije);
        return vmKategorija;
    }

    public void delete(String id) throws IOException {
        upisListeUFile(
                fetchAll().stream()
                        .filter((element) -> !element.getId().equals(id))
                        .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<VMKategorija> vmKategorije) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(vmKategorije).getBytes());

    }

    @Override
    protected List<Object> readData() throws FileNotFoundException {
        return g.fromJson(new JsonReader(new FileReader(FILE_PATH)),new TypeToken<List<VMKategorija>>() {}.getType());

    }
}
