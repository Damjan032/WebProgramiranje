package dao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import models.KorisnikNalog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Initializer {
    Object generateData()
    {
        return new ArrayList<>();
    }

    protected abstract List<Object> readData() throws FileNotFoundException;

    protected Object load(String path){
        try {

            File f = new File(path);
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    System.out.println("NEMOGUĆE JE KREIRATI  FILE: "+path);
                } else {
                    generateData();
                }
            }
            JsonReader reader = new JsonReader(new FileReader(f));
            List<Object> res = readData();
            if (res == null || res.isEmpty()){
                return generateData();
            }else{
                return res;
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
