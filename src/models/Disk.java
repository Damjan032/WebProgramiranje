package models;

import models.enums.TipDiska;


public class Disk extends Resurs {
    private TipDiska tip;
    private double kapacitet;
    private String vm;


    public Disk(String id, String ime, TipDiska tip, double kapacitet, String vm) {
        super(id, ime);
        this.tip = tip;
        this.kapacitet = kapacitet;
        this.vm = vm;
    }


    public TipDiska getTip() {
        return tip;
    }

    public void setTip(TipDiska tip) {
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
