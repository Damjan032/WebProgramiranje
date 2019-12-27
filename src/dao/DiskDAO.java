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
import java.util.UUID;
import java.util.stream.Collectors;
import models.Disk;

public class DiskDAO {

    private Gson g = new Gson();
    private static String FILE_PATH = "./data/disk.json";

    public List<Disk> fetchAll() {
        try {
            JsonReader reader = new JsonReader(new FileReader(FILE_PATH));
            return g.fromJson(reader, new TypeToken<List<Disk>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            throw new InternalServerErrorException("File " + FILE_PATH + " ne postoji.");
        }
    }

    public Disk fetchById(String id) {
        return fetchAll().stream().filter(disk -> disk.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }

    public Disk create(Disk disk) throws IOException {
        List<Disk> list = fetchAll();
        disk.setId(UUID.randomUUID().toString());
        list.add(disk);
        upisListeUFile(list);
        return disk;
    }

    public Disk update(Disk disk, String id) throws IOException {
        List<Disk> diskovi = fetchAll();
        diskovi.forEach(
            oldDisk -> {
                if (oldDisk.getId().equals(id)) {
                    oldDisk.setIme(disk.getIme());
                    oldDisk.setKapacitet(disk.getKapacitet());
                    oldDisk.setTip(disk.getTip());
                    oldDisk.setVm(oldDisk.getVm());
                }
            });

        upisListeUFile(diskovi);
        return disk;
    }

    public void delete(String id) throws IOException {
        upisListeUFile(
            fetchAll().stream()
                .filter((element) -> !element.getId().equals(id))
                .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<Disk> diskovi) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(diskovi).getBytes());

    }
}
