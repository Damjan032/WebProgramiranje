package dto;

import models.Aktivnost;
import models.Organizacija;
import models.enums.Uloga;

import java.util.List;

public class KorisnikDTO {

    private String email, ime, prezime;
    private String organizacija;
    private Uloga uloga;
    private List<Aktivnost> aktivnosti;

    private String ID;
    public static final class Builder {
        private String email, ime, prezime;
        private Organizacija organizacija;
        private Uloga uloga;
        private List<Aktivnost> aktivnosti;

        private String ID;

        public  Builder() {
        }

        public KorisnikDTO.Builder withId(String id) {
            this.ID = id;
            return this;
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

        public KorisnikDTO build() {
            KorisnikDTO korisnikDTO = new KorisnikDTO();
            korisnikDTO.ID = this.ID;
            korisnikDTO.ime = this.ime;
            korisnikDTO.prezime = this.prezime;
            korisnikDTO.email = this.email;
            korisnikDTO.aktivnosti = this.aktivnosti;
            return korisnikDTO;
        }
    }
}
