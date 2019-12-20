package models;

public abstract class Resurs {
    private String ime;


    public Resurs(String ime) {
        this.ime = ime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
}
