package models;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import com.google.gson.Gson;
import controllers.OrganizacijaController;
import controllers.VMKategorijeController;
import controllers.VirtuelnaMasinaController;
import dao.VirtuelnaMasinaDAO;
import exceptions.ExceptionsHandler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import models.komunikacija.KorisnikTrans;
import models.komunikacija.LoginPoruka;
import models.komunikacija.Poruka;
import services.VirtuelnaMasinaService;
import spark.Session;

public class Main {

    private static Gson g = new Gson();
    private static Sistem sistem;
    private static final String DATA_PATH = "./data/sistem.json";


    static boolean proveraPrijave(Session s) {
        System.out.println(s.attribute("user") != null);
        if (s.attribute("user") == null) {
            return false;
        } else {
            return true;
        }
    }

    public static void load() throws IOException {
        File f = new File(DATA_PATH);
        if (!f.exists()) {
            if (f.createNewFile()) {
                System.out.println("File " + DATA_PATH + " created.");
            }
        }

        sistem = g.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream(f))), Sistem.class);
        if (sistem == null) {
            sistem = new Sistem();
        }
    }

    public static void main(String[] args) {
        try {
            load();

            staticFiles.externalLocation(new File("./static").getCanonicalPath());
            OrganizacijaController.getInstance().init();
            VirtuelnaMasinaController.getInstance().init();
            ExceptionsHandler.getInstance().init();
            VMKategorijeController.getInstance().init();
            get("/", (req, res) -> {
                Session s = req.session();
                Korisnik k = s.attribute("user");
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

            get("/getUserType", (req, res) -> {
                Session s = req.session();
                System.out.println(s.attribute("user") != null);
                Korisnik k = s.attribute("user");
                if (k == null) {
                    return "";
                } else {
                    return g.toJson(k.getUloga().toString());
                }
            });

            get("/getUserOrg", (req, res) -> {
                Session s = req.session();
                System.out.println(s.attribute("user") != null);
                Korisnik k = s.attribute("user");
                if (k == null) {
                    return "";
                } else {
                    return g.toJson(k.getOrganizacija());
                }
            });

            get("/hello", (req, res) -> {
                return "OK";
            });
            get("/login", (req, res) -> {
                Session session = req.session();
                if (session == null) {
                    session = req.session(true);
                }
                if (session.attribute("user") != null) {
                    return g.toJson(new LoginPoruka("Već ste prijavljeni", false));
                }
                String kime = req.queryParams("kime");
                String sifra = req.queryParams("sifra");
                LoginPoruka lp = sistem.login(kime, sifra);
                if (lp.isStatus()) {
                    session.attribute("user", lp.getKorisnik());
                }
                return g.toJson(lp);
            });

            get("/logout", (req, res) -> {
                Session session = req.session();
                if (session.attribute("user") == null) {
                    return g.toJson(new Poruka("Već ste odjavljeni!", false));
                }
                session.removeAttribute("user");
                session.invalidate();
                return g.toJson(new Poruka("Odjava uspešna.", true));
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

            get("/getKorisnici", (req, res) -> {
                Session session = req.session();
                Korisnik user = session.attribute("user");
                if (user == null) {
                    return g.toJson(false);
                }
                return g.toJson(sistem.getKorisnici(user));
            });

            post("/dodajKorisnika", (req, res) -> {
                Session s = req.session();
                if (proveraPrijave(s)) {
                    Korisnik user = s.attribute("user");
                    return g.toJson(sistem.dodajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
                }
                return new Poruka("Niste prijavljeni", false);
            });
            VirtuelnaMasinaService service = new VirtuelnaMasinaService();
            VirtuelnaMasinaDAO dao = new VirtuelnaMasinaDAO();

//            dao.create(new VirtualMachine(null, "naziv", "1", new ArrayList<>()));
//            dao.create(new VirtualMachine(null, "naziv2", "2", new ArrayList<>()));
//            dao.create(new VirtualMachine(null, "naziv3", "3", new ArrayList<>()));
//            dao.create(new VirtualMachine(null, "naziv4", "1", new ArrayList<>()));



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}