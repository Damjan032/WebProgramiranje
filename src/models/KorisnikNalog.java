package models;

public class KorisnikNalog {
    private Korisnik korisnik;
    private int sifraHash;

    public KorisnikNalog(Korisnik korisnik, int sifraHash) {
        this.korisnik = korisnik;
        this.sifraHash = sifraHash;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public int getSifraHash() {
        return sifraHash;
    }
}
