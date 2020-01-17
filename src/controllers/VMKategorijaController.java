package controllers;

import com.google.gson.Gson;
import services.VMKategorijaService;

import java.util.Optional;

import static spark.Spark.*;
import static spark.Spark.delete;

public class VMKategorijaController implements Controller {
    private static Gson g = new Gson();
    VMKategorijaService vmKategorijaService = new VMKategorijaService();

    private static VMKategorijaController instance = null;

    public static VMKategorijaController getInstance() {
        return  Optional.ofNullable(instance).orElseGet(VMKategorijaController::new);
    }
    @Override
    public void init() {
        get("/vmKategorije", (req, res) -> {
            res.type("application/json");
            return vmKategorijaService.fetchAll();});

        get("/vmKategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return vmKategorijaService.fetchById(id);
        });

        post("/vmKategorije", (req, res) -> {
            res.type("application/json");
            return vmKategorijaService.create(req.body());
        });

        put("/vmKategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return vmKategorijaService.update(req.body(), id);
        });

        delete("/vmKategorije/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            vmKategorijaService.delete(id);
            return "";
        });
    }
}
