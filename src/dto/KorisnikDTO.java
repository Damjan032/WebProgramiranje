package dto;

import models.Aktivnost;
import models.Organizacija;
import models.enums.Uloga;

import java.util.List;

public class KorisnikDTO {

    private String email, ime, prezime;
    private Organizacija organizacija;
    private Uloga uloga;
    private List<Aktivnost> aktivnosti;

    public static final class Builder {
        private String email, ime, prezime;
        private Organizacija organizacija;
        private Uloga uloga;
        private List<Aktivnost> aktivnosti;

        public  Builder() {
        }
        public KorisnikDTO.Builder withIme(String ime) {
            this.ime = ime;
            return this;
        }

        public KorisnikDTO.Builder withPrezime(String prezime) {
            this.prezime = prezime;
            return this;
        }

        public KorisnikDTO.Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public KorisnikDTO.Builder withAktivnosti(List<Aktivnost> aktivnosti) {
            this.aktivnosti = aktivnosti;
            return this;
        }

        public KorisnikDTO.Builder withOrganizacija(Organizacija organizacija) {
            this.organizacija = organizacija;
            return this;
        }

        public KorisnikDTO.Builder withUloga(Uloga uloga){
            this.uloga = uloga;
            return this;

        }

        public KorisnikDTO build() {
            KorisnikDTO korisnikDTO = new KorisnikDTO();
            korisnikDTO.ime = this.ime;
            korisnikDTO.prezime = this.prezime;
            korisnikDTO.email = this.email;
            korisnikDTO.aktivnosti = this.aktivnosti;
            korisnikDTO.organizacija = this.organizacija;
            return korisnikDTO;
        }
    }
}
