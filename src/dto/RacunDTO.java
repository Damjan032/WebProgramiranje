package dto;

import models.Organizacija;

import java.time.LocalDate;

public class RacunDTO {
    String id;
    Organizacija org;
    LocalDate pocetak, zavrsetak;
    Double cena;

    public RacunDTO(String id, Organizacija org, LocalDate pocetak, LocalDate zavrsetak, Double cena) {
        this.id = id;
        this.org = org;
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
        this.cena = cena;
    }
}
