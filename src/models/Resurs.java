package models;

import models.enums.TipResursa;

public class Resurs {
    private String id;
    private String ime;
    private TipResursa tip;


    public Resurs(String id, String ime) {
        this.id = id;
        this.ime = ime;

    }

    public Resurs(String id, TipResursa tip) {
        this.id = id;
        this.tip = tip;
    }
    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TipResursa getTip() {
        return tip;
    }

    public void setTip(TipResursa tip) {
        this.tip = tip;
    }
}
