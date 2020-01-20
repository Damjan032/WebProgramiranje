package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import exceptions.BadRequestException;
import exceptions.InternalServerErrorException;
import exceptions.NotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import models.Disk;
import models.Korisnik;
import models.KorisnikNalog;
import models.VirtuelnaMasina;
import models.enums.Uloga;

public class DiskDAO extends Initializer {

    private Gson g = new Gson();
    private static String FILE_PATH = "./data/disk.json";
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO= new VirtuelnaMasinaDAO();


    public List<Disk> fetchAll() {
        return (List<Disk>) load(FILE_PATH);

    }

    public Disk fetchById(String id) {
        return fetchAll().stream().filter(disk -> disk.getId().equals(id)).findFirst().orElseThrow(NotFoundException::new);
    }

    public Disk fetchByName(String ime) {
        return fetchAll().stream().filter(disk -> disk.getIme().equals(ime)).findFirst().orElseThrow(NotFoundException::new);
    }

    public Disk create(Disk disk) throws IOException {
        disk.setId(UUID.randomUUID().toString());
        if (disk.getVm()!=null) {
            VirtuelnaMasinaDAO vmDAO = new VirtuelnaMasinaDAO();
            VirtuelnaMasina vm = vmDAO.fetchById(disk.getVm());
            vm.getDiskovi().add(disk.getId());
            vmDAO.update(vm, vm.getId());
        }
        List<Disk> list = fetchAll();
        list.add(disk);
        upisListeUFile(list);
        return disk;
    }

    public Disk update(Disk disk, String id) throws IOException {
        List<Disk> diskovi = fetchAll();
        if (!disk.getIme().equals(disk.getIme())){
            if(diskovi.stream().anyMatch(disk1 -> disk.getIme().equals(disk1.getIme()))){
                throw new BadRequestException("Već postoji disk sa tim imenom");
            }
        }
        diskovi.forEach(
            oldDisk -> {
                if (oldDisk.getId().equals(id)) {
                    if(KorisnikDAO.checkStringAttribute(disk.getIme())) {
                        oldDisk.setIme(disk.getIme());
                    }
                    if (disk.getKapacitet()!=0) {
                        oldDisk.setKapacitet(disk.getKapacitet());
                    }
                    if (disk.getTipDiska()!=null) {
                        oldDisk.setTipDiska(disk.getTipDiska());
                    }
                    if (disk.getVm()!=null) {
                        oldDisk.setVm(disk.getVm());
                    }
                }
            });

        upisListeUFile(diskovi);
        return disk;
    }

    public void delete(String id) throws IOException {
        Disk d = fetchById(id);
        VirtuelnaMasina vm = virtuelnaMasinaDAO.fetchById(d.getVm());
        vm.getDiskovi().remove(id);
        virtuelnaMasinaDAO.update(vm, vm.getId());
        upisListeUFile(
            fetchAll().stream()
                .filter((element) -> !element.getIme().equals(id))
                .collect(Collectors.toList()));
    }

    private void checkFile(){
        try {

            File f = new File(FILE_PATH);
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    System.out.println("NEMOGUĆE JE KREIRATI  FILE: "+FILE_PATH);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void upisListeUFile(List<Disk> diskovi) throws IOException {
        Files.write(Paths.get(FILE_PATH), g.toJson(diskovi).getBytes());

    }

    @Override
    Object generateData() {
        return new ArrayList<>();
    }

    @Override
    protected List<Object> readData() throws FileNotFoundException {
        return g.fromJson(new JsonReader(new FileReader(FILE_PATH)),new TypeToken<List<Disk>>() {}.getType());
    }
}
