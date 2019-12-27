package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.NotFoundException;
import models.Korisnik;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class KorisnikDAO {
    private Gson g = new Gson();
    private static String FILE_PATH = "./data/korisnici.json";

    public List<Korisnik> fetchAll() throws FileNotFoundException {
        // TODO: Čitanje svih organizacija iz fajla
        File f = new File(FILE_PATH);
        if(!(f.exists())){
            try {
                if(f.createNewFile()){
                    System.out.println("NEMOGUĆE JE KREIRATI FAJL");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("NEMOGUĆE JE KREIRATI FAJL");
            }
        }
        JsonReader reader = new JsonReader(new FileReader(f));
        return g.fromJson(reader, new TypeToken<List<Korisnik>>(){}.getType());
    }

    public Korisnik fetchById(UUID id) throws IOException {
        return fetchAll().stream().filter(korisnik -> korisnik.getID().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }
    public Optional<Korisnik> fetchByEmail(String ime) throws IOException {
        return fetchAll().stream().filter(korisnik -> korisnik.getEmail().equals(ime)).findFirst();
    }


    public Korisnik create(Korisnik korisnik) throws IOException {
        List<Korisnik> list = fetchAll();
        korisnik.setID(UUID.randomUUID());
        list.add(korisnik);
        upisListeUFile(list);
        return korisnik;
    }

    public Korisnik update(Korisnik korisnik, UUID id) throws IOException {
        List<Korisnik> korisnici = fetchAll();
        korisnici.forEach(
                oldOrg -> {
                    if (oldOrg.getID().equals(id)) {
                        oldOrg.setIme(korisnik.getIme());
                        oldOrg.setPrezime(korisnik.getPrezime());
                        oldOrg.setEmail(korisnik.getEmail());
                        oldOrg.setUloga(korisnik.getUloga());
                    }
                });

        upisListeUFile(korisnici);
        return korisnik;
    }

    public void delete(UUID id) throws IOException {
        upisListeUFile(
                fetchAll().stream()
                        .filter((element) -> !element.getID().equals(id))
                        .collect(Collectors.toList()));
    }

    private void upisListeUFile(List<Korisnik> korisnici) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(korisnici).getBytes());

    }
}
