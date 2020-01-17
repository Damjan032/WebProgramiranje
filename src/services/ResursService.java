package services;

import com.google.gson.Gson;
import dao.DiskDAO;
import dto.DiskDTO;
import dto.ResursDTO;
import models.Disk;
import models.enums.TipResursa;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResursService implements Service<String, String> {
    private DiskDAO diskDAO = new DiskDAO();
    private Gson g = new Gson();

    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        List<Disk> diskovi = diskDAO.fetchAll();
        List<ResursDTO> resursi = new ArrayList<>();
        diskovi.forEach(disk -> {
            resursi.add(new ResursDTO(disk.getId(),disk.getIme(), TipResursa.DISK));
        });
        return resursi.stream().map(resursDTO -> g.toJson(resursDTO)).collect(Collectors.toList());
    }

    @Override
    public String fetchById(String s) throws IOException {
        return null;
    }

    @Override
    public String create(String s) throws IOException {
        return null;
    }

    @Override
    public String update(String body, String s) throws IOException {
        return null;
    }

    @Override
    public void delete(String id) throws IOException {

    }
}
