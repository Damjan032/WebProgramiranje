package models;

import models.enums.TipResursa;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

public class MesecniRacun {
    String id;
    String org;
    LocalDate pocetak, zavrsetak;
    Double cena;

    public MesecniRacun(String org, LocalDate pocetak, LocalDate zavrsetak, Double cena) {
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

    public LocalDate getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDate pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDate getZavrsetak() {
        return zavrsetak;
    }

    public void setZavrsetak(LocalDate zavrsetak) {
        this.zavrsetak = zavrsetak;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }
}
