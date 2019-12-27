package controllers;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import java.util.Optional;
import services.OrganizacijaService;
import services.VMKategorijeService;

public class VMKategorijeController implements Controller {

    private static Gson g = new Gson();
    VMKategorijeService vmKategorijeService = new VMKategorijeService();

    private static VMKategorijeController instance = null;

    public static VMKategorijeController getInstance() {
        return  Optional.ofNullable(instance).orElseGet(VMKategorijeController::new);
    }

    @Override
    public void init() {
//        before("/kategorije",
//            (req, res) -> Optional.ofNullable(req.session().attribute("user")).orElseThrow(UnauthorizedException::new));

        get("/kategorije", (req, res) -> {
            res.type("application/json");
            return vmKategorijeService.fetchAll();});

        get("/kategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return vmKategorijeService.fetchById(id);
        });

        post("/kategorije", (req, res) -> {
            res.type("application/json");
            return vmKategorijeService.create(req.body());
        });

        put("/kategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return vmKategorijeService.update(req.body(), id);
        });

        delete("/kategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            vmKategorijeService.delete(id);
            return "";
        });
    }
}
