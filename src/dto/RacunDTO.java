package dto;

import models.Organizacija;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RacunDTO {
    String id;
    Organizacija org;
    String pocetak, zavrsetak;
    Double cena;

    public RacunDTO(String id, Organizacija org, LocalDateTime pocetak, LocalDateTime zavrsetak, Double cena) {
        DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        this.id = id;
        this.org = org;
        if (pocetak!=null) {
            this.pocetak = pocetak.format(FORMATER);
        }
        if (zavrsetak!=null) {
            this.zavrsetak = zavrsetak.format(FORMATER);
        }
        this.cena = cena;
    }
}
