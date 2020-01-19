package komunikacija;

import models.enums.TipDiska;

public class DiskTrans {
    private String ime;
    private String tip;
    private double kapacitet;
    private String vm;

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }


    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public double getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(double kapacitet) {
        this.kapacitet = kapacitet;
    }

    public String getVm() {
        return vm;
    }

    public void setVm(String vm) {
        this.vm = vm;
    }
}
