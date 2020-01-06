package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class DiskService implements Service<String, String> {
    @Override
    public List<String> fetchAll() throws FileNotFoundException {
        return null;
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
