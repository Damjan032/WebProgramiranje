package controllers;

import com.google.gson.Gson;
import services.DiskService;

import java.util.Optional;

public class DiskController implements Controller  {
    private static Gson g = new Gson();
    DiskService diskService = new DiskService();

    private static DiskController instance = null;

    public static DiskController getInstance() {
        return  Optional.ofNullable(instance).orElseGet(DiskController::new);
    }
    @Override
    public void init() {

    }
}
