package uz.jl.repository.atm;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.atm.Atm;
import uz.jl.models.card.Card;
import uz.jl.repository.BaseRepository;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Juraev Nodirbek, вс 12:10. 12.12.2021
 */
public class AtmRepository extends BaseRepository<Atm> {
    public static Atm sessionAtm; // atm tanlansa shu joyga yozish kk
    private static AtmRepository instance;
    private static FileData<Atm> fileData = new FileData<>();
    private static String filePath = "src/main/resources/db/atm.json";
    private static Type type = new TypeToken<List<Atm>>() {
    }.getType();
    private static List<Atm> atms;

    private AtmRepository() {
    }

    public static AtmRepository getInstance() {
        if (instance == null) {
            instance = new AtmRepository();
        }
        return instance;
    }

    @Override
    protected List<Atm> getAll() {
        atms = fileData.myReader(filePath, type);
        return atms;
    }

    public  Atm findByLocation(String location) {
        List<Atm> atms = fileData.myReader(filePath, type);
        for (Atm atm :  atms) {
            if (atm.getLocation().equals(location)) {
                return atm;
            }
        }
        return null;
    }
}
