package services;

import jdk.jshell.spi.ExecutionControl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Service <T, ID> {
    List<T> fetchAll() throws FileNotFoundException;
    T fetchById(ID id) throws IOException;
    T create(String s) throws IOException, ExecutionControl.NotImplementedException;
    T update(String body, ID id) throws IOException;
    void delete(String id) throws IOException;
}
