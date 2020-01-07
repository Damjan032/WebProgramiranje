package dto;

import models.Aktivnost;
import models.Disk;
import models.VMKategorija;

import java.util.List;

public class VirtuelnaMasinaDTO extends ResursDTO {
    private VMKategorija kategorija;
    private List<Disk> diskovi;
    private List<Aktivnost> aktivnosti;

    public VirtuelnaMasinaDTO(String id, String ime, VMKategorija kategorija, List<Disk> diskovi, List<Aktivnost> aktivnosti) {
        super(id, ime);
        this.kategorija = kategorija;
        this.diskovi = diskovi;
        this.aktivnosti = aktivnosti;
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

    public List<Aktivnost> getAktivnosti() {
        return aktivnosti;
    }

    public void setAktivnosti(List<Aktivnost> aktivnosti) {
        this.aktivnosti = aktivnosti;
    }
}
