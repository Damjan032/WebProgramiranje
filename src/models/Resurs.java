package models;

import models.enums.TipResursa;

public class Resurs {
    protected String id;
    protected String ime;
    protected TipResursa tip;
    protected String organizacija;

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

    public String getOrganizacija() {
        return organizacija;
    }

    public void setOrganizacija(String organizacija) {
        this.organizacija = organizacija;
    }
}
