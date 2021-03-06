package models;


import java.util.List;
import models.enums.TipResursa;

public class Organizacija {
    private String id;
    private String ime;
    private String opis;
    private String imgPath; //ne znam da l je bolje samo putanju da cuvamo zbog fronta, a ne celu sliku
    private List<String> korisnici;
    private List<Resurs> resursi; // object privremeno dok ne provalim sta su resursi njemu


    public Organizacija(String id, String ime, String opis, String imgPath, List<String> korisnici, List<Resurs> resursi) {
        this.id = id;
        this.ime = ime;
        this.opis = opis;
        this.imgPath = imgPath;
        this.korisnici = korisnici;
        this.resursi = resursi;
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

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        if (imgPath != null) {
            this.imgPath = imgPath;
        }
    }

    public List<String> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<String> korisnici) {
        this.korisnici = korisnici;
    }

    public List<Resurs> getResursi() {
        return resursi;
    }

    public void setResursi(List<Resurs> resursi) {
        this.resursi = resursi;
    }
}
