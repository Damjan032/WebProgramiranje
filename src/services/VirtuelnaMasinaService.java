package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.VMKategorijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import dto.VirtuelnaMasinaDTO;
import exceptions.BadRequestException;
import jdk.jshell.spi.ExecutionControl;
import models.Aktivnost;
import models.Disk;
import models.VirtuelnaMasina;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VirtuelnaMasinaService implements Service<String, String> {
    private Gson g = new Gson();
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO = new VirtuelnaMasinaDAO();
    private VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();
    private DiskDAO diskDTO = new DiskDAO();

    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        return virtuelnaMasinaDAO.fetchAll().stream().map(this::mapToVirtuelnaMasinaDTOString).collect(Collectors.toList());
    }

    private String mapToVirtuelnaMasinaDTOString(VirtuelnaMasina virtuelnaMasina) {
        List<Disk> diskovi = new ArrayList<>();
        List<Aktivnost> aktivnosti = new ArrayList<>();
        DiskDAO korisnikDAO = new DiskDAO();

        List<String> diskoviID = virtuelnaMasina.getDiskovi();

        if (diskoviID != null) {
            diskoviID.forEach(diskID -> {
                diskovi.add(korisnikDAO.fetchById(diskID));

            });
        }
        return
                g.toJson(new VirtuelnaMasinaDTO(virtuelnaMasina.getId(), virtuelnaMasina.getIme(), vmKategorijaDAO.fetchById(virtuelnaMasina.getKategorija()), diskovi, aktivnosti));
    }

    @Override
    public String fetchById(String id) throws IOException {
        return mapToVirtuelnaMasinaDTOString(virtuelnaMasinaDAO.fetchById(id));
    }

    public boolean fetchByKatgegorijaId(String kategoriajId) throws IOException {
        return virtuelnaMasinaDAO.fetchByKategorijaID(kategoriajId).isPresent();
    }

    @Override
    public String create(String s) throws IOException, ExecutionControl.NotImplementedException {
        VirtuelnaMasina virtuelnaMasina = g.fromJson(s, VirtuelnaMasina.class);
        virtuelnaMasina.setDiskovi(new ArrayList<>());
        virtuelnaMasina.setAktivnosti(new ArrayList<>());
        if(virtuelnaMasina.getKategorija().equalsIgnoreCase("")){
            throw new BadRequestException("Izaberi kategoriju");
        }
        virtuelnaMasina.setKategorija(vmKategorijaDAO.fetchByIme(virtuelnaMasina.getKategorija()).get().getId());
        if (virtuelnaMasinaDAO.fetchByIme(virtuelnaMasina.getIme()).isPresent() ) {
            throw new BadRequestException("Kategorija VM sa imenom: " + virtuelnaMasina.getIme() +" posotji");
        }
        return g.toJson(virtuelnaMasinaDAO.create(virtuelnaMasina));
    }

    @Override
    public String update(String body, String id) throws IOException {
        VirtuelnaMasina virtuelnaMasina = g.fromJson(body, VirtuelnaMasina.class);
        String ime = virtuelnaMasina.getIme();
        if(virtuelnaMasina.getKategorija()==null){
            throw new BadRequestException("Izaberi kategoriju");
        }
        virtuelnaMasina.setKategorija(vmKategorijaDAO.fetchByIme(virtuelnaMasina.getKategorija()).get().getId());
        if (virtuelnaMasinaDAO.fetchByIme(virtuelnaMasina.getIme()).isPresent() && !virtuelnaMasinaDAO.fetchById(id).getIme().equalsIgnoreCase(ime)) {
            throw new BadRequestException("Kategorija VM sa imenom: " + virtuelnaMasina.getIme() +" posotji");
        }
        return g.toJson(virtuelnaMasinaDAO.update(virtuelnaMasina, id));
    }

    @Override
    public void delete(String id) throws IOException {
        if(!virtuelnaMasinaDAO.fetchById(id).getDiskovi().isEmpty()){
            throw new BadRequestException("Izabrana virtuelna masina ne moze da se obrise, poseduje diskove.");
        }
        virtuelnaMasinaDAO.delete(id);
    }

    public Object fetchFiltred(Request req) {
        return null;//fetchAll().stream().filter()
    }
}
