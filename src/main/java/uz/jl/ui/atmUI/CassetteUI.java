package uz.jl.ui.atmUI;

import lombok.Getter;
import uz.jl.configs.LangConfig;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.repository.atm.AtmRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.atm.CassettesService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.awt.*;
import java.util.Objects;

/**
 * @author Juraev Nodirbek, пт 14:43. 10.12.2021
 */
public class CassetteUI extends BaseAbstractUI implements BaseUI {
    static CassettesService cassettesService;
    public static Atm atm;

    static {
        cassettesService = CassettesService.getInstance();
    }


    @Override
    public void create() {
        String cassetteTypeStr = Input.getStr(LangConfig.get("Enter.cassette.type"));
        String currencyValue = Input.getStr(LangConfig.get("Enter.currency.value"));
        String currencyCount = Input.getStr(LangConfig.get("Enter.currency.count"));
        ResponseEntity<String> response = cassettesService.creatCassettes(cassetteTypeStr, currencyValue, currencyCount);
        show(response);
    }

    @Override
    public void block() {
        String moneyVal= Input.getStr("Enter Money value: ");
        ResponseEntity<String> response= CassettesService.block(moneyVal);
        show(response);
    }

    @Override
    public void unblock() {

    }

    @Override
    public void delete() {

    }

    @Override
    public boolean list() {
        if (AtmUI.getInstance().list()) {
            String local = Input.getStr("ATM location: ");
            atm = AtmRepository.getInstance().findByLocation(local);
            if (Objects.isNull(atm)) {
                show(new ResponseEntity<>("ATM not found", Status.HTTP_NOT_FOUND));
                return false;
            }
            for (Cassette cassette : atm.getCassette()) {
                Print.println(cassette);
            }
            return true;
        } else {
            show(new ResponseEntity<>("There is no ATMs", Status.HTTP_NOT_FOUND));
            return false;
        }
    }

    private static CassetteUI instance;

    private CassetteUI() {
    }

    public static CassetteUI getInstance() {
        if (instance == null) {
            instance= new CassetteUI();
        }
        return instance;
    }
}
