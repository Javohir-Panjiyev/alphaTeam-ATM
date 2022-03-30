package uz.jl.services.atm;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import uz.jl.configs.LangConfig;
import uz.jl.enums.CassetteType;
import uz.jl.enums.Money;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.repository.atm.AtmRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;
import uz.jl.ui.atmUI.CassetteUI;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uz.jl.enums.Status.ACTIVE;
import static uz.jl.enums.Status.BLOCKED;
import static uz.jl.services.atm.AtmService.NUM_OF_CASSETTES;

@Getter
public class CassettesService {
    private static FileData<Atm> fileData = new FileData<>();
    private static Type type = new TypeToken<List<Atm>>() {
    }.getType();
    private static String path = "src/main/resources/db/atm.json";

    static List<Cassette> cassettesData = new ArrayList<>(NUM_OF_CASSETTES);


    public static ResponseEntity<String> block(String val) {
        List<Cassette> cassettes = CassetteUI.atm.getCassette();
        for (Cassette cassette : cassettes) {
            if (cassette.getCurrencyValue().toString().equalsIgnoreCase(val)) {
                List<Atm> atms = fileData.myReader(path, type);
                for (Atm atm : atms) {
                    if (atm.equals(CassetteUI.atm)) {
                        for (Cassette cassette1 : atm.getCassette()) {
                            if (cassette1.equals(cassette)) {
                                cassette1.setStatus(BLOCKED);

                                return new ResponseEntity<>("Blocked", Status.HTTP_OK);
                            }
                        }
                    }
                }
                fileData.myWriter(atms,path);
            }
        }
        return new ResponseEntity<>("Fail", Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> unBlock(String val) {
        List<Cassette> cassettes = CassetteUI.atm.getCassette();
        for (Cassette cassette : cassettes) {
            if (cassette.getCurrencyValue().toString().equalsIgnoreCase(val)) {
                List<Atm> atms = fileData.myReader(path, type);
                for (Atm atm : atms) {
                    if (atm.equals(CassetteUI.atm)) {
                        for (Cassette cassette1 : atm.getCassette()) {
                            if (cassette1.equals(cassette)) {
                                cassette1.setStatus(ACTIVE);

                                return new ResponseEntity<>("Actived", Status.HTTP_OK);
                            }
                        }
                    }
                }
                fileData.myWriter(atms,path);
            }
        }
        return new ResponseEntity<>("Fail", Status.HTTP_FORBIDDEN);
    }

    public ResponseEntity<String> creatCassettes(String cassetteType, String currencyValue, String currencyCount) {
        CassetteType type = CassetteType.findByName(cassetteType);
        if (Objects.isNull(type)) {
            return new ResponseEntity<>(LangConfig.get("Wrong.input.of.type"), Status.HTTP_FORBIDDEN);
        }
        Money value = Money.findByValue(currencyValue, type);
        if (Objects.isNull(value)) {
            return new ResponseEntity<>(LangConfig.get("Wrong.input.value"), Status.HTTP_FORBIDDEN);
        }
        if (!CheckCassetteService.checkCurrencyCount(currencyCount)) {
            return new ResponseEntity<>(LangConfig.get("Wrong.input.count"), Status.HTTP_FORBIDDEN);
        }
        Cassette newCassette = new Cassette(type, value, currencyCount);
        newCassette.setStatus(ACTIVE);
        cassettesData.add(newCassette);
        return new ResponseEntity<>("OK", Status.HTTP_OK);
    }

    private static CassettesService instance;

    private CassettesService() {
    }

    public static CassettesService getInstance() {
        if (instance == null) {
            instance = new CassettesService();
        }
        return instance;
    }
}
