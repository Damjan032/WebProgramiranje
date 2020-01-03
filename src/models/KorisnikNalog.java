package models;

import models.enums.Uloga;

public class KorisnikNalog {
    private Korisnik korisnik;
    private int sifraHash;

    public KorisnikNalog(Korisnik korisnik, int sifraHash) {
        this.korisnik = korisnik;
        this.sifraHash = sifraHash;
    }

    public KorisnikNalog(String email, String ime, String prezime, String organizacija, Uloga fromString, int sifraHash) {
        korisnik = new Korisnik(email, ime, prezime, organizacija, fromString);
        this.sifraHash = sifraHash;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public int getSifraHash() {
        return sifraHash;
    }
}
