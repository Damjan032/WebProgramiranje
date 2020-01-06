package controllers;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;

import java.util.Optional;

import services.OrganizacijaService;
public class OrganizacijaController implements Controller {

    private static Gson g = new Gson();
    OrganizacijaService organizacijaService = new OrganizacijaService();

    private static OrganizacijaController instance = null;

    public static OrganizacijaController getInstance() {
        return  Optional.ofNullable(instance).orElseGet(OrganizacijaController::new);
    }

    @Override
    public void init() {
//        before("/organizacije",
//            (req, res) -> Optional.ofNullable(req.session().attribute("user")).orElseThrow(UnauthorizedException::new));

        get("/organizacije", (req, res) -> {
            res.type("application/json");
            return organizacijaService.fetchAll();});

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
            return organizacijaService.updateWithImage(req, id);
        });

        delete("/organizacije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            organizacijaService.delete(id);
            return "";
        });
    }
}
