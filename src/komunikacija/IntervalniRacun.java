package komunikacija;

import java.time.LocalDate;
import java.util.List;

public class IntervalniRacun {
    public IntervalniRacun(List<ResursRacun> resursRacuni, LocalDate pocetak, LocalDate kraj, Double ukupnaCena) {
        this.resursRacuni = resursRacuni;
        this.pocetak = pocetak;
        this.kraj = kraj;
        this.ukupnaCena = ukupnaCena;
    }

    List<ResursRacun> resursRacuni;
    LocalDate pocetak, kraj;
    Double ukupnaCena;

    public List<ResursRacun> getResursRacuni() {
        return resursRacuni;
    }

    public void setResursRacuni(List<ResursRacun> resursRacuni) {
        this.resursRacuni = resursRacuni;
    }

    public LocalDate getPocetak() {
        return pocetak;
    }

    public void setPocetak(LocalDate pocetak) {
        this.pocetak = pocetak;
    }

    public LocalDate getKraj() {
        return kraj;
    }

    public void setKraj(LocalDate kraj) {
        this.kraj = kraj;
    }

    public Double getUkupnaCena() {
        return ukupnaCena;
    }

    public void setUkupnaCena(Double ukupnaCena) {
        this.ukupnaCena = ukupnaCena;
    }
}
