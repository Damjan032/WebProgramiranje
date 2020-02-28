package komunikacija;

import dto.KorisnikDTO;
import models.Korisnik;

public class LoginPoruka extends Poruka {
    Korisnik k;

    public LoginPoruka(String poruka, boolean status) {
        super(poruka, status);

    }

    public LoginPoruka(String poruka, boolean status, Korisnik k) {
        super(poruka, status);
        this.k = k;
    }

    public Poruka toPoruka() {
        return this;
    }

    public Korisnik getKorisnik() {
        return k;
    }
}
