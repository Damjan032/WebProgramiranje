package models;


import java.util.List;

public class Organizacija {
    private String ime, opis;
    private String imgPath; //ne znam da l je bolje samo putanju da cuvamo zbog fronta, a ne celu sliku
    private List<Korisnik> korisnici;
    private List<Resurs> resursi; // object privremeno dok ne provalim sta su resursi njemu


    public Organizacija(String ime, String opis, String imgPath, List<Korisnik> korisnici, List<Resurs> resursi) {
        this.ime = ime;
        this.opis = opis;
        this.imgPath = imgPath;
        this.korisnici = korisnici;
        this.resursi = resursi;
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
        this.imgPath = imgPath;
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public List<Resurs> getResursi() {
        return resursi;
    }

    public void setResursi(List<Resurs> resursi) {
        this.resursi = resursi;
    }
}
