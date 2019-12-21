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
                    return "OK";
                }
                return null;
            });
            get("/isloggedin", (req, res) -> {
                Session s = req.session();
                if(s.attribute("user")==null){
                    return g.toJson(false);
                }else{
                    return g.toJson(true);
                }
            });
            get("/hello", (req, res) -> {return "OK";});
            get("/login", (req, res) -> {
                Session session = req.session();
                if(session.attribute("user")!=null){
                    return g.toJson(new LoginPoruka("Već ste prijavljeni", false));
                }
                String kime = req.params("kime");
                String sifra = req.params("sifra");
                LoginPoruka lp = sistem.login(kime, sifra);
                if(lp.isStatus()) {
                    session.attribute("user", lp.getK());
                    res.redirect("/vmpregled.html");
                    return "OK";
                }
                return g.toJson(lp.toPoruka());
            });

            get("/logout", (req, res) -> {
                Session session = req.session();
                if(session.attribute("user")!=null){
                    res.body(g.toJson(new Poruka("Već ste odjavljeni!", false)));
                    return "";
                }
                session.invalidate();
                res.redirect("/login.html");
                return "OK";});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}