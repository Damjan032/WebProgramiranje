package models;

import java.util.List;

public class VirtualMachine extends Resurs {
    private String kategorija;
    private List<String> diskovi;
    private List<Aktivnost> aktivnosti;


    public VirtualMachine(String id, String ime, String kategorija, List<String> diskovi) {
        super(id, ime);
        this.kategorija = kategorija;
        this.diskovi = diskovi;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public List<String> getDiskovi() {
        return diskovi;
    }

    public void setDiskovi(List<String> diskovi) {
        this.diskovi = diskovi;
    }
}
