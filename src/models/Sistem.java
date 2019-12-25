package models;

import komunikacija.KorisnikTrans;
import komunikacija.LoginPoruka;
import komunikacija.Poruka;
import moduli.KorisniciModul;
import moduli.OrganizacijeModul;

import java.util.List;

public class Sistem {
    private OrganizacijeModul orgController = OrganizacijeModul.getInstance();
    private KorisniciModul korisniciModul;

    public OrganizacijeModul getOrgController() {
        return orgController;
    }


    public Sistem(){
        orgController = new OrganizacijeModul();
        korisniciModul = new KorisniciModul();
    }

    public LoginPoruka login(String username, String password) {
        if (!korisniciModul.korisnikRegistrovan(username)) {
            return new LoginPoruka("Korisnik sa korisničkim imenom: \"" + username + "\" ne postoji", false);
        }
        KorisnikNalog kn = korisniciModul.get(username);
        if (kn.getSifraHash() == password.hashCode()) {
            return new LoginPoruka("Uspešno prijavljicanje", true, kn.getKorisnik());
        }

        return new LoginPoruka("Pogrešna šifra!", false);
    }

    public List<Korisnik> getKorisnici(Korisnik user) {
        return korisniciModul.getKorisnici(user);
    }

    public Poruka dodajKorisnika(Korisnik user, KorisnikTrans kt) {
        return korisniciModul.dodajKorisnika(user,kt);
    }

    public KorisniciModul getKorisniciModul() {
        return korisniciModul;
    }

    public void setKorisniciModul(KorisniciModul korisniciModul) {
        this.korisniciModul = korisniciModul;
    }


    public Korisnik getKorisnik(Korisnik user, String email) {
        return korisniciModul.getKorisnik(user, email);
    }

    public Poruka azurirajKorisnika(Korisnik user, KorisnikTrans fromJson) {
        return korisniciModul.azurirajKorisnika(user, fromJson);

    }
}
