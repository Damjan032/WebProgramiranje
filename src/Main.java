import com.google.gson.Gson;
import controllers.KorisnikController;
import controllers.LoginController;
import controllers.OrganizacijaController;
import komunikacija.KorisnikTrans;
import komunikacija.LoginPoruka;
import komunikacija.Poruka;
import models.Korisnik;
import spark.Session;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import static spark.Spark.*;

public class Main {

    private static Gson g = new Gson();
    private static final String DATA_PATH = "./data/sistem.json";


    static boolean proveraPrijave(Session s){
        System.out.println(s.attribute("user")!=null);
        return s.attribute("user") != null;
    }

    public static void main(String[] args) {
        try {
            staticFiles.externalLocation(new File("./static").getCanonicalPath());

            OrganizacijaController organizacijaController = new OrganizacijaController();
            KorisnikController korisnikController = new KorisnikController();
            LoginController loginController = new LoginController();

            organizacijaController.init();
            korisnikController.init();
            loginController.init();

            get("/", (req, res) -> {
                Session s = req.session();
                Korisnik k = s.attribute("korisnik");
                if (k == null) {
                    res.redirect("/login.html");
                    return null;
                }
                res.redirect("/vmpregled.html");
                return null;
            });

            //DEPRECATED
            get("/isloggedin", (req, res) -> {
                Session s = req.session();
                return g.toJson(proveraPrijave(s));
            });



            get("/data/img/:image", (req, res) -> {
                String slika = req.params(":image");
                Path path = Paths.get("data/img/" + slika);
                String type = slika.split("\\.")[1];
                byte[] data = null;
                try {
                    data = Files.readAllBytes(path);
                } catch (Exception e1) {

                    e1.printStackTrace();
                }
                HttpServletResponse raw = res.raw();

                res.type("image/" + type);
                try {
                    raw.getOutputStream().write(data);
                    raw.getOutputStream().flush();
                    raw.getOutputStream().close();
                } catch (Exception e) {

                    e.printStackTrace();
                }
                return raw;
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
