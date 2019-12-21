package models;

import com.google.gson.Gson;
import spark.Session;

import java.io.File;

import static spark.Spark.*;

public class Main {

    static Gson g = new Gson();

    public static void main(String[] args) {
        try {
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
                    res.body(g.toJson(false));
                }else{
                    res.body(g.toJson(true));
                }
                return "OK";});
            get("/hello", (req, res) -> {return "OK";});
            get("/login", (req, res) -> {return "OK";});
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}