package models;

import models.enums.Uloga;

import java.util.IllegalFormatCodePointException;

public class KorisnikNalog {
    private Korisnik korisnik;
    private Integer sifraHash;

    public KorisnikNalog(Korisnik korisnik, int sifraHash) {
        this.korisnik = korisnik;
        this.sifraHash = sifraHash;
    }

    public KorisnikNalog(String email, String ime, String prezime, String organizacija, Uloga fromString, String sifra) {
        korisnik = new Korisnik(email, ime, prezime, organizacija, fromString);
        if (sifra!=null){
            this.sifraHash = sifra.hashCode();
        }else{
            this.sifraHash = null;
        }
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public Integer getSifraHash() {
        return sifraHash;
    }

    public void setSifraHash(Integer sifraHash) {
        this.sifraHash = sifraHash;
    }
}
