package controllers;

import com.google.gson.Gson;
import services.DiskService;

import java.util.Optional;

import static spark.Spark.*;
import static spark.Spark.delete;

public class DiskController implements Controller  {
    private static Gson g = new Gson();
    DiskService diskService = new DiskService();

    private static DiskController instance = null;

    public static DiskController getInstance() {
        return  Optional.ofNullable(instance).orElseGet(DiskController::new);
    }
    @Override
    public void init() {
        get("/diskovi", (req, res) -> {
            res.type("application/json");
            return diskService.fetchAll(req);
        });

        get("/diskovi/:id", (req, res) -> {
            res.type("application/json");
            return diskService.fetchById(req);
        });

        post("/diskovi", (req, res)->{
            res.type("application/json");
            return diskService.create(req);
        });

        put("/diskovi", (req, res)->{
            res.type("application/json");
            return g.toJson(diskService.update(req));
        });
        delete("/diskovi/:id",(req, res)->{
            res.type("application/json");
            return diskService.delete(req);
        });
    }
}
