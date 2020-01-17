package models;

public class VMKategorija
{
    private String id;
    private String ime;
    private int brJezgra;
    private double RAM; //gb
    private int brGPU;

    public VMKategorija(String id, String ime, int brJezgra, double ram, int brGPU) {
        this.id = id;
        this.ime = ime;
        this.brJezgra = brJezgra;
        RAM = ram;
        this.brGPU = brGPU;
    }

    public int getBrGPU() {
        return brGPU;
    }

    public void setBrGPU(int brGPU) {
        this.brGPU = brGPU;
    }

    public double getRAM() {
        return RAM;
    }

    public void setRAM(double RAM) {
        this.RAM = RAM;
    }

    public int getBrJezgra() {
        return brJezgra;
    }

    public void setBrJezgra(int brJezgra) {
        this.brJezgra = brJezgra;
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
