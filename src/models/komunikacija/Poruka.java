package models.komunikacija;

public class Poruka {
    String poruka;
    boolean status;

    public Poruka(String poruka, boolean status) {
        this.poruka = poruka;
        this.status = status;
    }

    public String getPoruka() {
        return poruka;
    }

    public boolean isStatus() {
        return status;
    }
}
