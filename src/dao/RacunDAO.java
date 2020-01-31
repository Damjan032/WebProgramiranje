package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.NotFoundException;
import models.KorisnikNalog;
import models.MesecniRacun;
import models.Organizacija;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class RacunDAO extends Initializer {
    private static final String FILE_PATH = "./data/racuni.json";
    private Gson g = new Gson();

    public List<MesecniRacun> create(MesecniRacun mesecniRacun){
            var racuni = fetchAll();
            mesecniRacun.setId(UUID.randomUUID().toString());
            racuni.add(mesecniRacun);
        try {
            upisListeUFile(racuni);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return racuni;
    }

    public MesecniRacun update(MesecniRacun mesecniRacun, String id){
        var racuni = fetchAll();
        for (MesecniRacun racun :
                racuni) {
            if (racun.getId().equals(id)) {
                if (mesecniRacun.getPocetak() != null) {
                    racun.setPocetak(mesecniRacun.getPocetak());
                }
                if (mesecniRacun.getZavrsetak() != null) {
                    racun.setZavrsetak(mesecniRacun.getZavrsetak());
                }
                if (mesecniRacun.getCena() != null) {
                    racun.setCena(mesecniRacun.getCena());
                }
                if (mesecniRacun.getOrg() != null) {
                    racun.setOrg(mesecniRacun.getOrg());
                }
            }
            try {
                upisListeUFile(racuni);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return racun;
        }
        throw new NotFoundException();
    }

    public List<MesecniRacun> fetchAll() {
        return (List<MesecniRacun>) load(FILE_PATH);
    }

    public MesecniRacun fetchById(String id){
        return fetchAll().stream().filter(racun->racun.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }

    @Override
    protected List<Object> readData() throws FileNotFoundException {
        return g.fromJson(new JsonReader(new FileReader(FILE_PATH)),new TypeToken<List<MesecniRacun>>() {}.getType());

    }
    private void upisListeUFile(List<MesecniRacun> mesecniRacuni) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(mesecniRacuni).getBytes());
    }
}
