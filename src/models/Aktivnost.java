package models;

import java.time.LocalDateTime;

public class Aktivnost {
    private LocalDateTime pocetak;
    private LocalDateTime zavrsetak;

    public Aktivnost(LocalDateTime pocetak, LocalDateTime zavrsetak) {
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
    }

    public LocalDateTime getZavrsetak() {
        return zavrsetak;
    }

    public void setZavrsetak(LocalDateTime zavrsetak) {
        this.zavrsetak = zavrsetak;
    }

    public LocalDateTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) {
        this.pocetak = pocetak;
    }
}
