package dto;

import java.util.List;
import models.Disk;
import models.Resurs;
import models.VMKategorija;

public class VirtuelnaMasinaDTO extends Resurs {

    private VMKategorija kategorija;
    private List<Disk> diskovi;

    public VirtuelnaMasinaDTO(String id, String ime, VMKategorija kategorija, List<Disk> diskovi) {
        super(id, ime);
        this.kategorija = kategorija;
        this.diskovi = diskovi;
    }

    public VMKategorija getKategorija() {
        return kategorija;
    }

    public void setKategorija(VMKategorija kategorija) {
        this.kategorija = kategorija;
    }

    public List<Disk> getDiskovi() {
        return diskovi;
    }

    public void setDiskovi(List<Disk> diskovi) {
        this.diskovi = diskovi;
    }


    public static final class Builder {

        private String id;
        private String ime;
        private VMKategorija kategorija;
        private List<Disk> diskovi;

        public Builder() {
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withIme(String ime) {
            this.ime = ime;
            return this;
        }

        public Builder withKategorija(VMKategorija kategorija) {
            this.kategorija = kategorija;
            return this;
        }

        public Builder withDiskovi(List<Disk> diskovi) {
            this.diskovi = diskovi;
            return this;
        }

        public VirtuelnaMasinaDTO build() {
            return new VirtuelnaMasinaDTO(id, ime, kategorija, diskovi);
        }
    }
}
