package dto;

import models.Organizacija;
import models.enums.TipResursa;

public class ResursDTO {
    protected String id;

    protected String ime;

    protected TipResursa tipResursa;
    protected Organizacija organizacija;
    public ResursDTO(String id, String ime) {
        this.id = id;
        this.ime = ime;
    }

    public ResursDTO(String id, String ime, TipResursa tipResursa) {
        this.id = id;
        this.ime = ime;
        this.tipResursa = tipResursa;
    }

    public ResursDTO() {

    }

    public TipResursa getTipResursa() {
        return tipResursa;
    }

    public void setTipResursa(TipResursa tipResursa) {
        this.tipResursa = tipResursa;
    }

    public Organizacija getOrganizacija() {
        return organizacija;
    }

    public void setOrganizacija(Organizacija organizacija) {
        this.organizacija = organizacija;
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


}
