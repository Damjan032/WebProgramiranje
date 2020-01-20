package services;

import jdk.jshell.spi.ExecutionControl;
import spark.Request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Service <T, ID> {
    List<T> fetchAll(Request req) throws FileNotFoundException;
    T fetchById(Request req,ID id) throws IOException;
    T create(Request req) throws IOException, ExecutionControl.NotImplementedException;
    T update(Request req, ID id) throws IOException;
    void delete(Request req,String id) throws IOException;
}
