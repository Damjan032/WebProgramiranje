package dto;

import java.util.List;
import models.Korisnik;
import models.Resurs;

public class OrganizacijaDTO {
    private String id;
    private String ime;
    private String opis;
    private String imgPath;
    private List<Korisnik> korisnici;
    private List<ResursDTO> resursi;


    public static final class Builder {
        private String id;

        private String ime;
        private String opis;
        private String imgPath;
        private List<Korisnik> korisnici;
        private List<ResursDTO> resursi;

        public  Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }
        public Builder withIme(String ime) {
            this.ime = ime;
            return this;
        }

        public Builder withOpis(String opis) {
            this.opis = opis;
            return this;
        }

        public Builder withImgPath(String imgPath) {
            this.imgPath = imgPath;
            return this;
        }

        public Builder withKorisnici(List<Korisnik> korisnici) {
            this.korisnici = korisnici;
            return this;
        }

        public Builder withResursi(List<ResursDTO> resursi) {
            this.resursi = resursi;
            return this;
        }

        public OrganizacijaDTO build() {
            OrganizacijaDTO organizacijaDTO = new OrganizacijaDTO();
            organizacijaDTO.id = this.id;
            organizacijaDTO.opis = this.opis;
            organizacijaDTO.korisnici = this.korisnici;
            organizacijaDTO.imgPath = this.imgPath;
            organizacijaDTO.resursi = this.resursi;
            organizacijaDTO.ime = this.ime;
            return organizacijaDTO;
        }
    }
}
