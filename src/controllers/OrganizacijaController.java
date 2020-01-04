package controllers;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import exceptions.UnauthorizedException;

import java.io.InputStream;
import java.util.Optional;
import services.OrganizacijaService;
import spark.Request;
import spark.Session;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;

public class OrganizacijaController implements Controller {

    private static Gson g = new Gson();
    OrganizacijaService organizacijaService = new OrganizacijaService();

    @Override
    public void init() {
//        before("/organizacije",
//            (req, res) -> Optional.ofNullable(req.session().attribute("user")).orElseThrow(UnauthorizedException::new));

        get("/organizacije", (req, res) -> {
            res.type("application/json");
            var v = organizacijaService.fetchAll();
            return v;});

        get("/organizacije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return organizacijaService.fetchById(id);
        });

        post("/organizacije", (req, res) -> {
            res.type("application/json");
            return organizacijaService.createWithImage(req);
        });

        put("/organizacije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return organizacijaService.update(req.body(), id);
        });

        delete("/organizacije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            organizacijaService.delete(id);
            return "";
        });

    }
}
