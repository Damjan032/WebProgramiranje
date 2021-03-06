package controllers;

import com.google.gson.Gson;
import services.VirtuelnaMasinaService;

import java.util.Optional;

import static spark.Spark.*;
import static spark.Spark.delete;

public class VirtuelnaMasinaController implements Controller {
    private static Gson g = new Gson();
    VirtuelnaMasinaService virtuelnaMasinaService = new VirtuelnaMasinaService();

    private static VirtuelnaMasinaController instance = null;

    public static VirtuelnaMasinaController getInstance() {
        return Optional.ofNullable(instance).orElseGet(VirtuelnaMasinaController::new);
    }

    @Override
    public void init() {
        get("/virtuelneMasine", (req, res) -> {
            res.type("application/json");
            if (req.queryParams() == null || req.queryParams().size() == 0) {
                return virtuelnaMasinaService.fetchAll(req);
            }
            return virtuelnaMasinaService.fetchFiltred(req);

        });

        get("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.fetchById(req,id);
        });

        get("/filtred", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.fetchById(req,id);
        });

        post("/virtuelneMasine", (req, res) -> {
            res.type("application/json");
            return virtuelnaMasinaService.create(req);
        });

        put("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.update(req, id);
        });

        put("/virtuelneMasine/activnost/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.updateActivnost(req, id);
        });

        delete("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            virtuelnaMasinaService.delete(req, id);
            return "";
        });
        delete("/virtuelneMasine/:id/:pocetakAktivnosti", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            String pocetakAktivnosti = req.params("pocetakAktivnosti");
            virtuelnaMasinaService.deleteAktivnost(req,id, pocetakAktivnosti);
            //virtuelnaMasinaService.delete(id);
            return "";
        });
        put("/virtuelneMasine/:id/:pocetakAktivnosti", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            String pocetakAktivnosti = req.params("pocetakAktivnosti");
            System.out.println("Dje ba raja" + pocetakAktivnosti);
            return virtuelnaMasinaService.updateActivnostTime(req, id, pocetakAktivnosti);
        });
        get("/virtuelneMasine/filtriraj/:naziv/:ramOd/:ramDo/:gpuOd/:gpuDo/:cpuOd/:cpuDo", (req, res) -> {
            res.type("application/json");
            String naziv = req.params("naziv");
            String ramOd = req.params("ramOd");
            System.out.println(naziv);
            return virtuelnaMasinaService.filtered(req);
        });
    }
}