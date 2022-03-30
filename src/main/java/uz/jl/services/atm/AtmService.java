package uz.jl.services.atm;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;
import uz.jl.ui.atmUI.CassetteUI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Juraev Nodirbek, чт 20:10. 09.12.2021
 */
@Getter
@Setter
@NoArgsConstructor
public class AtmService {
    public static final int NUM_OF_CASSETTES = 6;
    private static Atm atm = new Atm();
    private static FileData<Atm> fileData = new FileData<>();
    private static String file = "src/main/resources/db/atm.json";
    private static Type type = new TypeToken<List<Atm>>() {}.getType();
    private static List<Atm> atms;
    private static List<Cassette> cassettes;


    public static ResponseEntity<String> addAtm(String location) {
        atms = fileData.myReader(file, type);
        if(CheckUniqueLocation.checkUnique(atms, location)){
            atm.setLocation(location);
            atm.setCreatedBy( Session.getInstance().getUser().getId());
            atm.setStatus(uz.jl.enums.Status.ACTIVE);
            int number = 1;
            while (number <= NUM_OF_CASSETTES) {
              CassetteUI.getInstance().create();
                number++;
            }
            if (CassettesService.cassettesData.size() == NUM_OF_CASSETTES) {
                for (Cassette cassette : CassettesService.cassettesData) {
                    cassette.setAtmId(atm.getId());
                }
            } else {
                CassettesService.cassettesData = new ArrayList<Cassette>(NUM_OF_CASSETTES);
                return new ResponseEntity<>(LangConfig.get("Bad.input"), Status.HTTP_FORBIDDEN);
            }
            atm.setCassette(CassettesService.cassettesData);
            if (atm.getLocation().isEmpty()) {
                return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
            }
            if (atm.getCassette().isEmpty()) {
                return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);

            }
            atms.add(atm);
            fileData.myWriter(atms, file);
            return new ResponseEntity<>(LangConfig.get("SuccessfulLy"));
        }
        return new ResponseEntity<>(LangConfig.get("This.location.already.taken"), Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> blockAtm(String location){
        atms= fileData.myReader(file, type);
        for (Atm atm1:atms) {
            if (atm1.getLocation().equals(location)) {
                atm1.setStatus(uz.jl.enums.Status.BLOCKED);
                atm1.setUpdatedBy(Session.getInstance().getUser().getId());
                atm1.setUpdatedAt(new Date());
                fileData.myWriter(atms,file);
                return new ResponseEntity<>(LangConfig.get("SuccessfulLy"));
            }
        }
        return new ResponseEntity<>(LangConfig.get("This.ATM.not.found"), Status.HTTP_NOT_FOUND);
    }

    public static ResponseEntity<String> unBlockAtm(String location){
        atms= fileData.myReader(file, type);
        for (Atm atm1:atms) {
            if (atm1.getLocation().equals(location)) {
                atm1.setStatus(uz.jl.enums.Status.ACTIVE);
                atm1.setUpdatedBy(Session.getInstance().getUser().getId());
                atm1.setUpdatedAt(new Date());
                fileData.myWriter(atms,file);
                return new ResponseEntity<>(LangConfig.get("SuccessfulLy"));
            }
        }
        return new ResponseEntity<>(LangConfig.get("This.ATM.not.found"), Status.HTTP_NOT_FOUND);
    }

    public static List<Atm> list() {
        atms = fileData.myReader(file, type);
        return atms;
    }

    public static ResponseEntity<String> deleteATM(String location){
        atms= fileData.myReader(file, type);
        for (Atm o:atms) {
            if (o.getLocation().equals(location)){
                atms.remove(o);
                fileData.myWriter(atms,file);
                return new ResponseEntity<>( LangConfig.get("SuccessfulLy"));
            }
        }
       return new ResponseEntity<>(LangConfig.get("This.ATM.not.found"), Status.HTTP_NOT_FOUND);
    }
}
