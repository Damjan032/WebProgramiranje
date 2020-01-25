package models;

import java.util.List;

public class VirtuelnaMasina extends Resurs {
    private String kategorija;
    private List<String> diskovi;
    private List<Aktivnost> aktivnosti;
    private int CORES;
    private int RAM;
    private int GPUCORES;

    public VirtuelnaMasina(String id, String ime, String kategorija, List<String> diskovi, List<Aktivnost> aktivnosti) {
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

    public int getGPUCORES() {
        return GPUCORES;
    }

    public void setGPUCORES(int GPUCORES) {
        this.GPUCORES = GPUCORES;
    }

    public int getRAM() {
        return RAM;
    }

    public void setRAM(int RAM) {
        this.RAM = RAM;
    }

    public int getCORES() {
        return CORES;
    }

    public void setCORES(int CORES) {
        this.CORES = CORES;
    }
}
