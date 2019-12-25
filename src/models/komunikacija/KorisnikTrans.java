package models.komunikacija;

import models.enums.Uloga;

public class KorisnikTrans {

    private String email, ime, prezime, organizacija, sifra, uloga;


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
}
