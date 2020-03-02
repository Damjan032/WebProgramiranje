package dto;

import models.Aktivnost;
import models.Disk;
import models.Organizacija;
import models.VMKategorija;
import models.enums.TipResursa;

import java.util.List;

public class VirtuelnaMasinaDTO extends ResursDTO {
    private VMKategorija kategorija;
    private List<Disk> diskovi;
    private List<Aktivnost> aktivnosti;
    private boolean isActiv;

    public VirtuelnaMasinaDTO(String id, String ime, VMKategorija kategorija, List<Disk> diskovi, List<Aktivnost> aktivnosti, Organizacija o) {
        super(id, ime, TipResursa.VM);
        setOrganizacija(o);
        this.kategorija = kategorija;
        this.diskovi = diskovi;
        this.aktivnosti = aktivnosti;
    }

    public VirtuelnaMasinaDTO() {
        super();
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

    public void setIsActiv(){
        if(aktivnosti == null){
            this.isActiv= false;
        }
        else if(aktivnosti.isEmpty()){
            this.isActiv= false;
        }
        else if (aktivnosti.get(aktivnosti.size()-1).getZavrsetak()==null){
            this.isActiv= true;
        }
        else this.isActiv= false;
    }

    public void setIsActiv(boolean isActiv){
        this.isActiv = isActiv;
    }

    public boolean getIsActiv(){
        setIsActiv();
        return this.isActiv;
    }
}
