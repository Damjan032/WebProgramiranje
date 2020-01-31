package komunikacija;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class IntervalniRacun {
    public IntervalniRacun(List<ResursRacun> resursRacuni, LocalDateTime pocetak, LocalDateTime kraj, Double ukupnaCena) {
        this.resursRacuni = resursRacuni;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.ukupnaCena = ukupnaCena;
    }

    List<ResursRacun> resursRacuni;
    LocalDateTime pocetak, kraj;
    Double ukupnaCena;

    public LocalDateTime getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDateTime pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDateTime getKraj() {
        return kraj;
    }

    public void setKraj(LocalDateTime kraj) {
        this.kraj = kraj;
    }

    public List<ResursRacun> getResursRacuni() {
        return resursRacuni;
    }

    public void setResursRacuni(List<ResursRacun> resursRacuni) {
        this.resursRacuni = resursRacuni;
    }



    public Double getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(Double ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }
}
