package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.VMKategorijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import dto.VirtuelnaMasinaDTO;
import exceptions.BadRequestException;
import javaxt.utils.Array;
import models.Aktivnost;
import models.Disk;
import models.VirtuelnaMasina;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        VirtuelnaMasinaDTO virtuelnaMasinaDTO = new VirtuelnaMasinaDTO(virtuelnaMasina.getId(), virtuelnaMasina.getIme(),
                vmKategorijaDAO.fetchById(virtuelnaMasina.getKategorija()),
                diskovi,
                virtuelnaMasina.getAktivnosti()==null? aktivnosti : virtuelnaMasina.getAktivnosti());
        virtuelnaMasinaDTO.setIsActiv();
        return g.toJson(virtuelnaMasinaDTO);
    }

    @Override
    public String fetchById(String id) throws IOException {
        return mapToVirtuelnaMasinaDTOString(virtuelnaMasinaDAO.fetchById(id));
    }

    public boolean fetchByKatgegorijaId(String kategoriajId) throws IOException {
        return virtuelnaMasinaDAO.fetchByKategorijaID(kategoriajId).isPresent();
    }

    @Override
    public String create(String s) throws IOException {
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

    public String updateActivnost(String body, String id) throws IOException {
        VirtuelnaMasina virtuelnaMasina = virtuelnaMasinaDAO.fetchById(id);
        VirtuelnaMasinaDTO virtuelnaMasinaDTO = g.fromJson(body, VirtuelnaMasinaDTO.class);
        virtuelnaMasinaDTO.setAktivnosti(virtuelnaMasina.getAktivnosti());

        boolean aktivnost = virtuelnaMasinaDTO.getIsActiv();
        if(aktivnost==true){
            virtuelnaMasina.getAktivnosti().get(virtuelnaMasina.getAktivnosti().size()-1).setZavrsetak(LocalDateTime.now());
        }
        else{
            if(virtuelnaMasina.getAktivnosti()== null) {
                virtuelnaMasina.setAktivnosti(new ArrayList<>());
            }
            virtuelnaMasina.getAktivnosti().add(new Aktivnost(LocalDateTime.now(), null));
        }
        virtuelnaMasinaDTO.setIsActiv(!aktivnost);
        System.out.println( virtuelnaMasina.getAktivnosti().get(0).getPocetak());
        return g.toJson(virtuelnaMasinaDAO.update(virtuelnaMasina, id));
    }

    public String deleteAktivnost(String id, String pocetakAktivnosti) throws IOException {
        VirtuelnaMasina virtuelnaMasina = virtuelnaMasinaDAO.fetchById(id);
        Aktivnost a = virtuelnaMasina.getAktivnosti().stream().filter(aktivnost -> aktivnost.getPocetak().equalsIgnoreCase(pocetakAktivnosti)).findFirst().orElse(null);
        System.out.println(pocetakAktivnosti);
        if(a!=null){
            virtuelnaMasina.getAktivnosti().remove(a);
        }
        return g.toJson(virtuelnaMasinaDAO.update(virtuelnaMasina, id));
    }

    public String updateActivnostTime(String body, String id, String pocetakAktivnosti) throws IOException {
        VirtuelnaMasina virtuelnaMasina = virtuelnaMasinaDAO.fetchById(id);
        Aktivnost aktivnostNova = g.fromJson(body.replace('T', ' '), Aktivnost.class); //Ima T ispred vrremena u stringu koji je poslat sa fornta
        Aktivnost a = virtuelnaMasina.getAktivnosti().stream().filter(aktivnost -> aktivnost.getPocetak().equalsIgnoreCase(pocetakAktivnosti)).findFirst().orElse(null);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        boolean correctPocetak = false;
        boolean correctZavrsetak = false;
        LocalDateTime ldtPocetak = LocalDateTime.now();
        LocalDateTime ldtZavrsetak = LocalDateTime.now();
        if(!aktivnostNova.getPocetak().equalsIgnoreCase("")){
            ldtPocetak = LocalDateTime.parse(aktivnostNova.getPocetak(),dateFormatter);
            correctPocetak= true;
        }
        if(!aktivnostNova.getZavrsetak().equalsIgnoreCase("")){
            ldtZavrsetak = LocalDateTime.parse(aktivnostNova.getZavrsetak(),dateFormatter);
            correctZavrsetak= true;
        }
        if(correctPocetak && correctZavrsetak && ldtZavrsetak.isAfter(ldtPocetak)){
            int index = virtuelnaMasina.getAktivnosti().indexOf(a);
            virtuelnaMasina.getAktivnosti().remove(a);
            virtuelnaMasina.getAktivnosti().add(index, aktivnostNova);
        }else if(correctPocetak && LocalDateTime.parse(a.getZavrsetak(), dateFormatter).isAfter(ldtPocetak)){
            a.setPocetak(aktivnostNova.getPocetak());
        }else if(correctZavrsetak && LocalDateTime.parse(a.getPocetak(), dateFormatter).isBefore(ldtZavrsetak)){
            a.setZavrsetak(aktivnostNova.getZavrsetak());
        }else{
            throw new BadRequestException("Datum zavrsetka mora biti pre datuma pocetka");
        }
        return g.toJson(virtuelnaMasinaDAO.update(virtuelnaMasina, id));
    }
}