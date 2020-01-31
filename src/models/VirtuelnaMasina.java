package models;

import java.util.ArrayList;
import java.util.List;

public class VirtuelnaMasina extends Resurs {
    private String kategorija;
    private List<String> diskovi;
    private List<Aktivnost> aktivnosti;

    public VirtuelnaMasina(String id, String ime, String kategorija, List<String> diskovi, List<Aktivnost> aktivnosti, String organizacija) {
        super(id, ime);
        this.kategorija = kategorija;
        this.diskovi = diskovi;
        this.aktivnosti = aktivnosti;
    }

    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public List<String> getDiskovi() {
        if (diskovi==null){
            diskovi=new ArrayList<>();
        }
        return diskovi;
    }

    public void setDiskovi(List<String> diskovi) {
        this.diskovi = diskovi;
    }

    public List<Aktivnost> getAktivnosti() {
        return aktivnosti;
    }

    public void setAktivnosti(List<Aktivnost> aktivnosti) {
        this.aktivnosti = aktivnosti;
    }

}
