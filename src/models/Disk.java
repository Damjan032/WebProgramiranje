package models;

import models.enums.TipDiska;


public class Disk extends Resurs {
    private TipDiska tip;
    private double kapacitet;
    private VirtualMachine vm;


    public Disk(String ime, TipDiska tip, double kapacitet, VirtualMachine vm) {
        super(ime);
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

    public VirtualMachine getVm() {
        return vm;
    }

    public void setVm(VirtualMachine vm) {
        this.vm = vm;
    }
}
