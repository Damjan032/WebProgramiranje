import com.google.gson.Gson;
import komunikacija.KorisnikTrans;
import komunikacija.LoginPoruka;
import komunikacija.Poruka;
import models.Korisnik;
import models.Sistem;
import spark.Session;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static spark.Spark.*;

public class Main {

    private static Gson g = new Gson();
    private static Sistem sistem;
    private static final String DATA_PATH = "./data/sistem.json";


    static boolean proveraPrijave(Session s){
        System.out.println(s.attribute("user")!=null);
        if(s.attribute("user")==null){
            return false;
        }else{
            return true;
        }
    }

    public static void load() throws IOException {
        File f = new File(DATA_PATH);
        if(!f.exists()){
            if(f.createNewFile()){
                System.out.println("File "+DATA_PATH+" created.");
            }
        }

        manipulacijaKorisnicima();

        sistem = g.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream(f))), Sistem.class);
        if (sistem == null) {
            sistem = new Sistem();
        }
    }

    public static void main(String[] args) {
        try {
            load();

            staticFiles.externalLocation(new File("./static").getCanonicalPath());
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
                if(session.attribute("user")!=null){
                    return g.toJson(new LoginPoruka("Već ste prijavljeni", false));
                }
                String kime = req.queryParams("kime");
                String sifra = req.queryParams("sifra");
                LoginPoruka lp = sistem.login(kime, sifra);
                if(lp.isStatus()) {
                    session.attribute("user", lp.getKorisnik());
                }
                return g.toJson(lp);
            });

            get("/logout", (req, res) -> {
                Session session = req.session();
                if(session.attribute("user")==null){
                    return g.toJson(new Poruka("Već ste odjavljeni!", false));
                }
                session.removeAttribute("user");
                session.invalidate();
                return g.toJson(new Poruka("Odjava uspešna.", true));
            });

            post("/orgAddSubmit", (req, res) -> {
                Session session = req.session();
                String ime, opis, type;

                req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));

                Part iIme = req.raw().getPart("oIme"); //Ovo ne radi
                ime = req.queryParams("oIme");
                opis = req.queryParams("oOpis");
                type = req.queryParams("nazivSlike").split("\\.")[1];
                System.out.println("TIP" + type);

                InputStream file = req.raw().getPart("oSlika").getInputStream();
                Poruka p = sistem.getOrgController().addOrg(ime,opis, file, type);
                if (p.isStatus()){

                }

                return g.toJson(p);
            });

            get("/getOrganizacije", (req, res) -> {
                Session session = req.session();
                Korisnik user = session.attribute("user");
                if (user == null) {
                    return g.toJson(false);
                }
                return g.toJson(sistem.getOrgController().getOrganizacije());
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

    public static void manipulacijaKorisnicima(){
        get("/tipKorisnika", (req, res) -> {
            Session s = req.session();
            System.out.println(s.attribute("user") != null);
            Korisnik k = s.attribute("user");
            if (k == null) {
                return "";
            } else {
                return g.toJson(k.getUloga().toString());
            }
        });
        get("/korisnici", (req, res) -> {
            Session session = req.session();
            Korisnik user = session.attribute("user");
            if(user == null) {
                return g.toJson(false);
            }
            return g.toJson(sistem.getKorisnici(user));
        });

        get("/korisnici/:email", (req, res) -> {
            Session session = req.session();
            Korisnik user = session.attribute("user");
            if(user == null) {
                return g.toJson(false);
            }
            String email = req.params(":email");
            return g.toJson(sistem.getKorisnik(user,email));
        });

        post("/korisnici", (req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.dodajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        });
        put("/korisnici", (req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.azurirajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        });
        delete("/korisnici",(req, res)->{
            Session s = req.session();
            if(proveraPrijave(s)){
                Korisnik user = s.attribute("user");
                return g.toJson(sistem.azurirajKorisnika(user, g.fromJson(req.body(), KorisnikTrans.class)));
            }
            return new Poruka("Niste prijavljeni", false);
        });
    }

}
