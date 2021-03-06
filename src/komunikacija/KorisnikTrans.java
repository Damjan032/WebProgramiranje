package komunikacija;

import models.Aktivnost;

import java.util.List;

public class KorisnikTrans {

    private String email;
    private String ime;
    private String prezime;
    private String organizacija;
    private String sifra;
    private String uloga;
    private List<Aktivnost> aktivnosti;

    public KorisnikTrans(String email, String ime, String prezime, String organizacija, String sifra, String uloga) {
        this.email = email;
        this.ime = ime;
        this.prezime = prezime;
        this.organizacija = organizacija;
        this.sifra = sifra;
        this.uloga = uloga;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getOrganizacija() {
        return organizacija;
    }

    public void setOrganizacija(String organizacija) {
        this.organizacija = organizacija;
    }

    public String getSifra() {
        return sifra;
    }

    public void setSifra(String sifra) {
        this.sifra = sifra;
    }

    public String getUloga() {
        return uloga;
    }

    public List<Aktivnost> getAktivnosti() {
        return null;
    }

    public void setAktivnosti(List<Aktivnost> aktivnosti) {
        this.aktivnosti = aktivnosti;
    }
}
