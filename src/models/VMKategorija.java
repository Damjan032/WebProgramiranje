package models;

public class VMKategorija {
    private String id;
    private String ime;
    private Integer brJezgra;
    private Double RAM; //gb
    private Integer brGPU;

    public VMKategorija(String id, String ime, Integer brJezgra, Double RAM, Integer brGPU) {
        this.id = id;
        this.ime = ime;
        this.brJezgra = brJezgra;
        this.RAM = RAM;
        this.brGPU = brGPU;
    }

    public VMKategorija() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
