package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Aktivnost {
    private String pocetak;
    private String zavrsetak;
    public Aktivnost(String pocetak, String zavrsetak) {
        this.pocetak = pocetak;
        this.zavrsetak = zavrsetak;
    }
    public Aktivnost(LocalDateTime pocetak, LocalDateTime zavrsetak) {
        DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.pocetak = pocetak.format(FORMATER);
        if(zavrsetak!=null)
            this.zavrsetak = zavrsetak.format(FORMATER);
    }
    public String getZavrsetak() {
        return zavrsetak;
    }

    public void setZavrsetak(LocalDateTime zavrsetak) {
        DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.zavrsetak = zavrsetak.format(FORMATER);
    }

    public void setZavrsetak(String zavrsetak){
        this.zavrsetak= zavrsetak;
    }

    public void setPocetak(String pocetak){
        this.pocetak = pocetak;
    }
    public String getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) {
        DateTimeFormatter FORMATER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.pocetak = pocetak.format(FORMATER);
    }
}
