package controllers;

import dao.RacunDAO;
import services.RacunService;

import static spark.Spark.get;

public class RacunController implements Controller{

    RacunService racunService = new RacunService();

    @Override
    public void init() {
        get("/racuni",(req,res)->{
            res.type("aplication/json");
            return racunService.fetchAll(req);
        });
        get("/racuni/:id",(req, res)->{
            res.type("aplication/json");
            String id = req.params("id");
            return racunService.fetchById(id,req);
        });
        get("/racuni/:pocetak/:kraj",(req, res)->{
            res.type("aplication/json");
            String pocetak = req.params("pocetak");
            String kraj = req.params("kraj");
            return racunService.fetchIntervalRacun(pocetak, kraj,req);
        });
    }
}
