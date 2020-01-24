package komunikacija;

import models.Disk;
import models.enums.TipDiska;

public class DiskTrans {

    Disk disk;
    private String org;

    public TipDiska getTipDiska() {
        return disk.getTipDiska();
    }

    public void setTipDiska(TipDiska tipDiska) {
        this.disk.setTipDiska(tipDiska);
    }

    public double getKapacitet() {
        return disk.getKapacitet();
    }

    public void setKapacitet(double kapacitet) {

        disk.setKapacitet(kapacitet);
    }

    public String getVm() {
        return disk.getVm();
    }

    public void setVm(String vm) {
    disk.setVm(vm);
    }

    public String getId() {
        return disk.getId();
    }

    public void setId(String id) {
        disk.setId(id);
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getIme() {
        return disk.getIme();
    }

    public void setIme(String ime) {
        disk.setIme(ime);
    }

    public Disk getDisk() {

        return disk;
    }
}
