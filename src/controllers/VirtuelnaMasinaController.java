package controllers;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import services.VirtuelnaMasinaService;
import spark.QueryParamsMap;

public class VirtuelnaMasinaController implements Controller {

    private static Gson g = new Gson();
    VirtuelnaMasinaService virtuelnaMasinaService = new VirtuelnaMasinaService();

    private static VirtuelnaMasinaController instance = null;

    public static VirtuelnaMasinaController getInstance() {
        return Optional.ofNullable(instance).orElseGet(VirtuelnaMasinaController::new);
    }

    @Override
    public void init() {
//        before("/virtuelneMasine",
//            (req, res) -> Optional.ofNullable(req.session().attribute("user")).orElseThrow(UnauthorizedException::new));

        get("/virtuelneMasine", (req, res) -> {
            res.type("application/json");
            Map<String, String[]> queryMap = req.queryMap().toMap();
            if (queryMap.isEmpty()) {
                return virtuelnaMasinaService.fetchAll();
            } else {
                return virtuelnaMasinaService.search(queryMap);
            }
        });

        get("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.fetchById(id);
        });

        post("/virtuelneMasine", (req, res) -> {
            res.type("application/json");
            return virtuelnaMasinaService.create(req.body());
        });

        put("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return virtuelnaMasinaService.update(req.body(), id);
        });

        delete("/virtuelneMasine/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            virtuelnaMasinaService.delete(id);
            return "";
        });
    }
}
