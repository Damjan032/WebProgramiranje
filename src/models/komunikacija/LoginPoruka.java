package models.komunikacija;

import models.Korisnik;

public class LoginPoruka extends Poruka{
    Korisnik k;

    public LoginPoruka(String poruka, boolean status) {
        super(poruka, status);

    }

    public LoginPoruka(String poruka, boolean status, Korisnik k) {
        super(poruka, status);
        this.k = k;
    }


    public Korisnik getKorisnik() {
        return k;
    }

    public Poruka toPoruka() {
        return this;
    }
}
