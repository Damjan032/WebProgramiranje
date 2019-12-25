package moduli;

import models.Organizacija;
import komunikacija.Poruka;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class OrganizacijeModul {
    private static OrganizacijeModul orgControler = null;
    private ArrayList<Organizacija> organizacije = new ArrayList<>();

    public ArrayList<Organizacija> getOrganizacije() {
        return organizacije;
    }

    public void setOrganizacije(ArrayList<Organizacija> organizacije) {
        this.organizacije = organizacije;
    }

    public OrganizacijeModul() {
        this.organizacije = new ArrayList<>();
    }

    public static OrganizacijeModul getInstance() {
        if (orgControler == null) {
            orgControler = new OrganizacijeModul();
            orgControler.organizacije.add(new Organizacija("Pera", "Mnogo dobra", "data/img/pera.png", new ArrayList<>(), new ArrayList<>()));
        }

        return orgControler;
    }

    public Poruka addOrg(String ime, String opis, InputStream imgFile, String type) throws IOException {
        String[] types = {"png", "jpg", "gif"};
        if (!Arrays.stream(types).anyMatch(type::equals)) {
            return new Poruka("Molimo Vas izaberiti sliku (jpg, png, gif).", false);
        }
        for (Organizacija o : organizacije) {
            if (o.getIme().equalsIgnoreCase(ime)) {
                return new Poruka("Organizacija je vec uneta", false);
            }
        }
        ;

        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgFile.readAllBytes()));
        File outputfile = new File("data/img/" + ime.toLowerCase().trim() + "." + type);
        ImageIO.write(img, type, outputfile);

        organizacije.add(new Organizacija(ime, opis, "data/img/" + ime.toLowerCase().trim() + "." + type, new ArrayList<>(), new ArrayList<>()));
        return new Poruka("Uspesno uneta organizacija", true);
    }
}
