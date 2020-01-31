package dto;

import models.Organizacija;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RacunDTO {
    String id;
    Organizacija org;
    LocalDateTime pocetak, zavrsetak;
    Double cena;

    public RacunDTO(String id, Organizacija org, LocalDateTime pocetak, LocalDateTime zavrsetak, Double cena) {
        this.id = id;
        this.org = org;
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
        this.cena = cena;
    }
}
