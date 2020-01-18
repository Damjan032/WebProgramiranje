package models;

import models.enums.TipDiska;


public class Disk extends Resurs {
    private TipDiska tipDiska;
    private double kapacitet;
    private String vm;


    public Disk(String id, String ime, TipDiska tip, double kapacitet, String vm) {
        super(id, ime);
        this.tipDiska = tip;
        this.kapacitet = kapacitet;
        this.vm = vm;
    }


    public TipDiska getTipDiska() {
        return tipDiska;
    }

    public void setTipDiska(TipDiska tipDiska) {
        this.tipDiska = tipDiska;
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
