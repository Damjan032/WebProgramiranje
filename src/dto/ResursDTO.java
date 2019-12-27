package dto;

import models.enums.TipResursa;

public abstract class ResursDTO {
    protected String id;

    protected String ime;

    protected TipResursa tipResursa;

    public ResursDTO(String id, String ime) {
        this.id = id;
        this.ime = ime;
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
