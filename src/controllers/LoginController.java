package controllers;

import com.google.gson.Gson;
import komunikacija.LoginPoruka;
import komunikacija.Poruka;
import services.LoginService;
import spark.Session;

import static spark.Spark.get;

public class LoginController implements Controller{

    private static Gson g = new Gson();
    LoginService loginService = new LoginService();

    @Override
    public void init() {

        get("/korisnik", (req, res)->{
            return g.toJson(loginService.getKorisnik(req));
        });

        get("/login", (req, res) -> {
            return g.toJson(loginService.tryLogin(req));
        });

        get("/logout", (req, res) -> {
            return g.toJson(loginService.tryLogout(req));
        });
    }


}
