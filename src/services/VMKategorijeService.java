package services;

import com.google.gson.Gson;
import dao.VMKategorijaDAO;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import models.VMKategorija;

public class VMKategorijeService implements Service<String, String> {

    private Gson g = new Gson();
    private VMKategorijaDAO vmKategorijaDAO = new VMKategorijaDAO();

    @Override
    public List<String> fetchAll() {
        return vmKategorijaDAO.fetchAll().stream().map((vm) -> g.toJson(vm)).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String id) {
        return g.toJson(vmKategorijaDAO.fetchById(id));
    }

    @Override
    public String create(String req) throws IOException {
        return g.toJson(vmKategorijaDAO.create(g.fromJson(req, VMKategorija.class)));
    }

    @Override
    public String update(String organizacija, String id) throws IOException {
        return g.toJson(vmKategorijaDAO.update(g.fromJson(organizacija, VMKategorija.class), id));
    }

    @Override
    public void delete(String id) throws IOException {
        vmKategorijaDAO.delete(id);
    }
}

