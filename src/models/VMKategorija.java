package models;

public class VMKategorija {
    private String ime;
    private int brJezgra;
    private double RAM; //gb
    private int brGPU;

    public VMKategorija(String ime, int brJezgra, double RAM, int brGPU) {
        this.ime = ime;
        this.brJezgra = brJezgra;
        this.RAM = RAM;
        this.brGPU = brGPU;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public int getBrJezgra() {
        return brJezgra;
    }

    public void setBrJezgra(int brJezgra) {
        this.brJezgra = brJezgra;
    }

    public double getRAM() {
        return RAM;
    }

    public void setRAM(double RAM) {
        this.RAM = RAM;
    }

    public int getBrGPU() {
        return brGPU;
    }

    public void setBrGPU(int brGPU) {
        this.brGPU = brGPU;
    }
}
