package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Service <T, ID> {
    List<T> fetchAll() throws FileNotFoundException;
    T fetchById(ID id) throws FileNotFoundException;
    T create(String s) throws IOException;
    T update(String body, ID id) throws IOException;
    void delete(String id) throws IOException;
}