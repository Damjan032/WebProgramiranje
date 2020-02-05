package services;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import dao.*;
import dto.RacunDTO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import komunikacija.Cenovnik;
import komunikacija.IntervalniRacun;
import komunikacija.ResursRacun;
import models.*;
import models.enums.TipDiska;
import models.enums.TipResursa;
import models.enums.Uloga;
import spark.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;


public class RacunService {


    private static final String CENOVNIK_PATH = "./data/cenovnik.json";
    RacunDAO racunDAO = new RacunDAO();
    private OrganizacijaDAO orgDAO = new OrganizacijaDAO();
    private VirtuelnaMasinaDAO virtuelnaMasinaDAO = new VirtuelnaMasinaDAO();
    private DiskDAO diskDAO = new DiskDAO();
    private Cenovnik cenovnik;
    Gson g = new Gson();
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RacunService(){
        Thread t = new Thread(this::obracunajRacune);
        t.start();
        File f = new File(CENOVNIK_PATH);
        if (!f.exists()){
            cenovnik = new Cenovnik(25.0,15.0,1.0,0.1,0.3);
            try {
                Files.write(Paths.get(CENOVNIK_PATH), g.toJson(cenovnik).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            try {
                cenovnik = g.fromJson(new JsonReader(new FileReader(CENOVNIK_PATH)), Cenovnik.class);
                if (cenovnik.getVmCUDA()==null){
                    cenovnik = new Cenovnik(25.0,15.0,1.0,0.1,0.3);
                    Files.write(Paths.get(CENOVNIK_PATH),g.toJson(cenovnik).getBytes());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void obracunajRacune(){
        while (true){
            System.out.println("[RacuniThread] updating racuni "+LocalDate.now().toString());
            for (Organizacija o: orgDAO.fetchAll()){
                List<MesecniRacun> racuni = racunDAO.fetchAll().stream().filter(racun->racun.getOrg().equals(o.getId())).collect(Collectors.toList());
                LocalDateTime today = LocalDateTime.now();
                if (racuni.isEmpty()){
                    MesecniRacun racun = new MesecniRacun(o.getId(), today,null, 0.0);
                    racunDAO.create(racun);
                }else {
                    racuni = racuni.stream().sorted(Comparator.comparing(MesecniRacun::getPocetak)).collect(Collectors.toList());
                    MesecniRacun mesecniRacun = racuni.get(racuni.size() - 1);
                    if (mesecniRacun.getZavrsetak() == null) {
                        if (DAYS.between(mesecniRacun.getPocetak(), today) >= 30) {
                            LocalDateTime zavrsetak = mesecniRacun.getPocetak().plusDays(30);
                            IntervalniRacun ir = calculatePriceForOrg(o, mesecniRacun.getPocetak(), zavrsetak);
                            mesecniRacun.setZavrsetak(zavrsetak);
                            mesecniRacun.setCena(ir.getUkupnaCena());
                        } else {
                            IntervalniRacun ir = calculatePriceForOrg(o, mesecniRacun.getPocetak(), today);
                            mesecniRacun.setCena(ir.getUkupnaCena());
                            ;
                        }
                        racunDAO.update(mesecniRacun, mesecniRacun.getId());
                    } else {
                        LocalDateTime start = mesecniRacun.getZavrsetak();
                        while (DAYS.between(start, today) > 30) {
                            LocalDateTime zavrsetak = start.plusDays(30);
                            IntervalniRacun ir = calculatePriceForOrg(o, start, zavrsetak);
                            MesecniRacun mesecniRacun1 = new MesecniRacun(o.getId(), start, zavrsetak, ir.getUkupnaCena());
                            racunDAO.create(mesecniRacun1);
                            start = zavrsetak;
                        }
                        IntervalniRacun ir = calculatePriceForOrg(o, start, today);
                        MesecniRacun mesecniRacun1 = new MesecniRacun(o.getId(), start, null, ir.getUkupnaCena());
                        racunDAO.create(mesecniRacun1);
                    }
                }
            }
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public List<String> fetchAll(Request req){
        Korisnik k = req.session().attribute("korisnik");
        if (k.getUloga()!= Uloga.ADMIN){
            throw  new UnauthorizedException();
        }
        Organizacija o = orgDAO.fetchById(k.getOrganizacija());
        List<MesecniRacun> racuni = racunDAO.fetchAll();
        return racuni.stream().filter(racun->racun.getOrg().equals(o.getId())).map(this::mapToRacunDTOString).collect(Collectors.toList());
    }

    public String fetchById(String id, Request req) {
        Korisnik k = req.session().attribute("korisnik");
        if (k.getUloga()!= Uloga.ADMIN) {
            throw new UnauthorizedException();
        }
        return g.toJson(racunDAO.fetchById(id));
    }

    private String mapToRacunDTOString(MesecniRacun mesecniRacun){
        Organizacija o = orgDAO.fetchById(mesecniRacun.getOrg());
        return g.toJson(new RacunDTO(mesecniRacun.getId(), o, mesecniRacun.getPocetak(), mesecniRacun.getZavrsetak(), mesecniRacun.getCena()));
    }

    public String fetchIntervalRacun(String pocetak, String kraj, Request req) {
        Korisnik k = req.session().attribute("korisnik");
        if (k.getUloga()!= Uloga.ADMIN) {
            throw new UnauthorizedException();
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start1 = LocalDate.from(dateFormatter.parse(pocetak));
        LocalDateTime start = start1.atStartOfDay();
        LocalDate end1 = LocalDate.from(dateFormatter.parse(kraj));
        LocalDateTime end = end1.atTime(23, 59);
        if (end.isBefore(start)){
            throw new BadRequestException("Datum kraja mora biti posle datuma poÄ�etka.");
        }
        Organizacija o = orgDAO.fetchById(k.getOrganizacija());
        return g.toJson(calculatePriceForOrg(o, start ,end));
    }

    private IntervalniRacun calculatePriceForOrg(Organizacija o, LocalDateTime start, LocalDateTime end){
        long days = DAYS.between(start, end);
        List<Disk> diskovi = diskDAO.fetchAll().stream().filter(disk-> o.getResursi().stream().anyMatch(resurs -> resurs.getTip()== TipResursa.DISK&&resurs.getId().equals(disk.getId()))).collect(Collectors.toList());
        List<VirtuelnaMasina> virtuelnaMasine = virtuelnaMasinaDAO.fetchAll().stream().filter(virtuelnaMasina-> o.getResursi().stream().anyMatch(resurs -> resurs.getTip()== TipResursa.VM&&resurs.getId().equals(virtuelnaMasina.getId()))).collect(Collectors.toList());
        List<ResursRacun> resursRacuni = new ArrayList<>();
        double ukupnaCena = 0.0;
        for (Disk disk:
                diskovi) {
            double cena = calculateDiskPrice(disk,days);
            ukupnaCena+=cena;
            resursRacuni.add(new ResursRacun(disk.getIme(),TipResursa.DISK,cena));
        }
        for (VirtuelnaMasina vm:
                virtuelnaMasine) {
            double cena = calculateVMPrice(vm,start,end);
            ukupnaCena+=cena;
            resursRacuni.add(new ResursRacun(vm.getIme(), TipResursa.VM, cena));
        }
        return new IntervalniRacun(resursRacuni, start, end,ukupnaCena);
    }


    private double calculateVMPrice(VirtuelnaMasina vm, LocalDateTime start, LocalDateTime end) {


        double cena = 0.0;
        VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();
        VMKategorija vmKategorija = vmKategorijaDAO.fetchById(vm.getKategorija());
        for (Aktivnost a:vm.getAktivnosti()){
//            LocalDate startAktivnosti = LocalDate.parse(a.getPocetak());
            LocalDateTime startAktivnosti = LocalDateTime.from(dateFormatter.parse(a.getPocetak()));
            if(a.getZavrsetak()==null) {
            	continue;
            }
            LocalDateTime krajAktivnosti = LocalDateTime.from(dateFormatter.parse(a.getZavrsetak()));
            double hours;
            if (startAktivnosti.isAfter(start)){
                if (krajAktivnosti.isBefore(end)){
                    hours = HOURS.between(startAktivnosti,krajAktivnosti);
                }else{
                    hours = HOURS.between(startAktivnosti, end);
                }
            }else if (krajAktivnosti.isAfter(start)){
                    hours = HOURS.between(start, krajAktivnosti);
            }else{
                continue;
            }
            if (hours==0){
                hours = 1;
            }
            cena+= (hours)*(cenovnik.getVmCore()*vmKategorija.getBrJezgra()+cenovnik.getVmRAM()*vmKategorija.getRAM()+cenovnik.getVmCUDA()*vmKategorija.getBrGPU())/30/24;
        }
        return cena;
    }

    private double calculateDiskPrice(Disk d, long days){
        double modifier;
        if (d.getTipDiska()== TipDiska.SSD){
            modifier = cenovnik.getDiskSSD();
        }else
        {
            modifier = cenovnik.getDiskHDD();
        }
        return modifier*d.getKapacitet()*days/30;
    }
}
