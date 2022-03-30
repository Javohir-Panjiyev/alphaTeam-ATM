package uz.jl.services.filesystems;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 * @author Juraev Nodirbek, сб 23:07. 04.12.2021
 */
public class FileData<T> {

    public List<T> myReader(String fileName, Type get) {
        List<T> objList;
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)
        ) {
            String data = bufferedReader.lines().collect(Collectors.joining());
            objList = new Gson().fromJson(data, get);
            if (objList != null)
                return objList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void myWriter(List<T> elements, String fileName) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            bufferedWriter.write(new Gson().toJson(elements));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
