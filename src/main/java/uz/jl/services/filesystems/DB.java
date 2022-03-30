package uz.jl.services.filesystems;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uz.jl.models.auth.AuthUser;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Elmurodov Javohir, Wed 5:52 PM. 12/8/2021
 */
public class DB {
    private final static String usersFilePath = "src/main/resources/db/users.json";
    private static List<AuthUser> users;

    public static List<AuthUser> getUsers() {
        if (Objects.isNull(users)) {
            try (FileReader reader = new FileReader(usersFilePath);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                String jsonDATA = bufferedReader.lines().collect(Collectors.joining());
                users = new Gson().fromJson(jsonDATA, new TypeToken<List<AuthUser>>() {
                }.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static void writeUsers(List<AuthUser> users) {
        try (FileWriter fileWriter = new FileWriter(usersFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(new Gson().toJson(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
