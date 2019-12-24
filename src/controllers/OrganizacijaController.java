package controllers;

import models.Organizacija;
import models.komunikacija.Poruka;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class OrganizacijaController {
    private static OrganizacijaController orgControler = null;
    private ArrayList<Organizacija> organizacije = new ArrayList<>();

    public ArrayList<Organizacija> getOrganizacije() {
        return organizacije;
    }

    public void setOrganizacije(ArrayList<Organizacija> organizacije) {
        this.organizacije = organizacije;
    }

    private OrganizacijaController() {
        this.organizacije = new ArrayList<>();
    }

    public static OrganizacijaController getInstance(){
        if (orgControler == null)
            orgControler = new OrganizacijaController();

        return orgControler;
    }

    public Poruka addOrg(String ime, String opis, InputStream imgFile, String type) throws IOException {
        String[] types = {"png", "jpg", "gif"};
        if (!Arrays.stream(types).anyMatch(type::equals)){
            return new Poruka("Molimo Vas izaberiti sliku (jpg, png, gif).", false);
        }
        for (Organizacija o: organizacije) {
            if (o.getIme().equalsIgnoreCase(ime)) {
                return new Poruka("Organizacija je vec uneta", false);
            }
        };

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgFile.readAllBytes()));
        File outputfile = new File("data/img/" +ime+".png");
        ImageIO.write(img, type, outputfile);

        organizacije.add(new Organizacija(ime,opis,outputfile.getAbsolutePath(), new ArrayList<>(), new ArrayList<>()));
        return new Poruka("Uspesno uneta organizacija", true);
    }
}
