package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.NotFoundException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import models.Organizacija;

public class OrganizacijaDAO {
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/org.json";

    public List<Organizacija> fetchAll() throws FileNotFoundException {
        // TODO: Čitanje svih organizacija iz fajla
        JsonReader reader = new JsonReader(new FileReader(FILE_PATH));
        return g.fromJson(reader, new TypeToken<List<Organizacija>>(){}.getType());
    }

    public Organizacija fetchById(String id) throws FileNotFoundException {
        return fetchAll().stream().filter(organizacija -> organizacija.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }
    public Optional<Organizacija> fetchByIme(String ime) throws FileNotFoundException {
        return fetchAll().stream().filter(organizacija -> organizacija.getIme().equals(ime)).findFirst();
    }


    public Organizacija create(Organizacija organizacija) throws IOException {
        List<Organizacija> list = fetchAll();
        organizacija.setId(UUID.randomUUID().toString());
        list.add(organizacija);
        upisListeUFile(list);
        return organizacija;
    }

    public Organizacija update(Organizacija organizacija, String id) throws IOException {
        List<Organizacija> organizacije = fetchAll();
        organizacije.forEach(
            oldOrg -> {
                if (oldOrg.getId().equals(id)) {
                    oldOrg.setIme(organizacija.getIme());
                    oldOrg.setImgPath(organizacija.getImgPath());
                    oldOrg.setKorisnici(organizacija.getKorisnici());
                    oldOrg.setOpis(organizacija.getOpis());
                    oldOrg.setResursi(organizacija.getResursi());
                }
            });

        upisListeUFile(organizacije);
        return organizacija;
    }

    public void delete(String id) throws IOException {
        upisListeUFile(
            fetchAll().stream()
                .filter((element) -> !element.getId().equals(id))
                .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<Organizacija> organizacijas) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(organizacijas).getBytes());

    }
}
