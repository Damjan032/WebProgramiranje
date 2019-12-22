package models;

import com.google.gson.Gson;
import models.komunikacija.LoginPoruka;
import models.komunikacija.Poruka;
import spark.Session;

import java.io.*;

import static spark.Spark.*;

public class Main {

    private static Gson g = new Gson();
    private static Sistem sistem;
    private static final String DATA_PATH = "./data/sistem.json";
    public static void load() throws IOException {
        File f = new File(DATA_PATH);
        if(!f.exists()){
            if(f.createNewFile()){
                System.out.println("File "+DATA_PATH+" created.");
            }
        }

        sistem = g.fromJson(new BufferedReader(new InputStreamReader(new FileInputStream(f))), Sistem.class);
        if(sistem == null){
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
                if(k==null){
                    res.redirect("/login.html");
                    return null;
                }
                res.redirect("/vmpregled.html");
                return null;
            });
            get("/isloggedin", (req, res) -> {
                Session s = req.session();
                System.out.println(s.attribute("user")!=null);
                if(s.attribute("user")==null){
                    return g.toJson(false);
                }else{
                    return g.toJson(true);
                }
            });
            get("/hello", (req, res) -> {return "OK";});
            get("/login", (req, res) -> {
                Session session = req.session();
                if(session==null){
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

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}