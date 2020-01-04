package models;

import java.util.List;

public class VirtualMachine extends Resurs {
    private VMKategorija kategorija;
    private List<Disk> diskovi;


    public VirtualMachine(String id, String ime, VMKategorija kategorija, List<Disk> diskovi) {
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
}
