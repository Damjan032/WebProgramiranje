package models;

import models.enums.TipResursa;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public class MesecniRacun {
    String id;
    String org;
    LocalDateTime pocetak, zavrsetak;
    Double cena;

    public MesecniRacun(String org, LocalDateTime pocetak, LocalDateTime zavrsetak, Double cena) {
        this.org = org;
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
        this.cena = cena;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    public LocalDateTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDateTime getZavrsetak() {
        return zavrsetak;
    }

    public void setZavrsetak(LocalDateTime zavrsetak) {
        this.zavrsetak = zavrsetak;
    }
}
