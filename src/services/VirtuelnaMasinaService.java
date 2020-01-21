package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dao.OrganizacijaDAO;
import dao.VMKategorijaDAO;
import dao.VirtuelnaMasinaDAO;
import dto.DiskDTO;
import dto.VirtuelnaMasinaDTO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import javaxt.utils.Array;

import models.*;
import models.enums.TipResursa;
import models.enums.Uloga;
import models.Aktivnost;
import models.Disk;
import models.VMKategorija;
import models.VirtuelnaMasina;
import spark.Request;

import java.awt.image.VolatileImage;
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
    private OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();

    @Override
    public List<String> fetchAll(Request req) throws FileNotFoundException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        var vm = virtuelnaMasinaDAO.fetchAll();
        if (u == Uloga.SUPER_ADMIN){
            return vm.stream().map(vmasina -> g.toJson(vmasina, VirtuelnaMasina.class)).collect(Collectors.toList());
        }
        OrganizacijaDAO organizacijaDAO = new OrganizacijaDAO();
        Organizacija o = organizacijaDAO.
                fetchAll().
                stream().
                filter(org->
                        org.getKorisnici().contains(k.getId())
                ).findFirst().get();

        vm = vm.stream().filter(vms->orgContainsVm(o, vms)).collect(Collectors.toList());

        return vm.stream().map(this::mapToVirtuelnaMasinaDTOString).collect(Collectors.toList());
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
    public String fetchById(Request req, String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        if (u!=Uloga.SUPER_ADMIN){
            checkVMAccessPrivilege(k, id);
        }
        return mapToVirtuelnaMasinaDTOString(virtuelnaMasinaDAO.fetchById(id));
    }

    private void checkVMAccessPrivilege(Korisnik k, String id) {
        Organizacija o = organizacijaDAO.
                fetchAll().

                stream().
                filter(org->
                        org.getKorisnici().contains(k.getId())
                ).findFirst().get();
        VirtuelnaMasina vm = virtuelnaMasinaDAO.fetchById(id);
        if (!orgContainsVm(o,vm)){
            throw new UnauthorizedException();
        }
    }

    public boolean fetchByKatgegorijaId(String kategoriajId) throws IOException {
        return virtuelnaMasinaDAO.fetchByKategorijaID(kategoriajId).isPresent();
    }

    @Override
    public String create(Request req) throws IOException {
        //TODO kako se dodaje id u org
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        VirtuelnaMasina virtuelnaMasina = g.fromJson(req.body(), VirtuelnaMasina.class);
        if (u==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }else if(u==Uloga.ADMIN)
        {
            checkVMAccessPrivilege(k, virtuelnaMasina.getId());
        }
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
    public String update(Request req, String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        if (u==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }else if(u==Uloga.ADMIN)
        {
            checkVMAccessPrivilege(k, id);
        }
        String body = req.body();
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
    public void delete(Request req,String id) throws IOException {
        Korisnik k = req.session().attribute("korisnik");
        Uloga u = k.getUloga();
        if (u==Uloga.KORISNIK){
            throw new UnauthorizedException();
        }else if(u==Uloga.ADMIN)
        {
            checkVMAccessPrivilege(k, id);
        }
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

    private boolean orgContainsVm(Organizacija o, VirtuelnaMasina vms){
        for (Resurs r:
                o.getResursi()) {
            if (r.getTip()== TipResursa.VM && r.getId().equals(vms.getId())){
                return true;
            }
        }
        return false;
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

    public String filtered(Request req) {
        VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();
        int ramOd = Integer.parseInt(req.params("ramOd"));
        int ramDo = Integer.parseInt(req.params("ramDo"));

        int gpuOd = Integer.parseInt(req.params("gpuOd"));
        int gpuDo = Integer.parseInt(req.params("gpuDo"));

        int cpuOd = Integer.parseInt(req.params("cpuOd"));
        int cpuDo = Integer.parseInt(req.params("cpuDo"));

        String naziv = req.params("naziv");

        List<VirtuelnaMasina> filtriraneVm = new ArrayList<>();

        if(naziv.trim().equalsIgnoreCase(""))
            filtriraneVm = virtuelnaMasinaDAO.fetchAll();
        else
            filtriraneVm = virtuelnaMasinaDAO.fetchAll().stream().filter(vm -> vm.getIme().startsWith(naziv.trim())).collect(Collectors.toList());

        filtriraneVm = filtriraneVm.stream().filter(vma ->{
            VMKategorija kategorija = vmKategorijaDAO.fetchById(vma.getKategorija());
            boolean s1 = kategorija.getRAM()<=ramDo && kategorija.getRAM()>=ramOd;
            boolean s2 = kategorija.getBrGPU()<=gpuDo && kategorija.getBrGPU()>=gpuOd;
            boolean s3 = kategorija.getBrJezgra()<=cpuDo && kategorija.getBrJezgra()>=cpuOd;
            return (s1 && s2 && s3);
        }).collect(Collectors.toList());
        if(filtriraneVm.size()==0){
            throw new BadRequestException("Nema adekvatne vm");
        }
        System.out.println(filtriraneVm.size());
        return g.toJson(filtriraneVm);
    }
}
