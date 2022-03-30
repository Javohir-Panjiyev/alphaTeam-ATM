package uz.jl.ui.atmUI;

import uz.jl.configs.LangConfig;
import uz.jl.models.atm.Atm;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.atm.AtmService;
import uz.jl.services.atm.CassettesService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Elmurodov Javohir, Wed 4:51 PM. 12/8/2021
 */
public class AtmUI extends BaseAbstractUI implements BaseUI {

    private static AtmUI atmUI;

    public static AtmUI getInstance() {
        if (Objects.isNull(atmUI)) {
            return atmUI = new AtmUI();
        }
        return atmUI;
    }

    private AtmUI() {
    }

    public void blockCassette() {

    }

    public void unblockCassette() {
        if (CassetteUI.getInstance().list()) {
            String val = Input.getStr("Enter Money type: ");
            ResponseEntity<String> response = CassettesService.block(val);
            show(response);
        }
    }

    @Override
    public void create() {
        String location = Input.getStr(LangConfig.get("Enter.Atm.Location"));
        ResponseEntity<String> response = AtmService.addAtm(location);
        show(response);

    }

    @Override
    public void block() {
        String location = Input.getStr(LangConfig.get("Enter.Atm.Location"));
        ResponseEntity<String> response = AtmService.blockAtm(location);
        show(response);
    }

    @Override
    public void unblock() {
        String location = Input.getStr(LangConfig.get("Enter.Atm.Location"));
        ResponseEntity<String> response = AtmService.unBlockAtm(location);
        show(response);
    }

    @Override
    public void delete() {
        String location = Input.getStr(LangConfig.get("Enter.Atm.Location"));
        ResponseEntity<String> response = AtmService.deleteATM(location);
        show(response);
    }

    @Override
    public boolean list() {
        List<Atm> atms = AtmService.list();
        if (Objects.isNull(atms)) {
            show(new ResponseEntity<String>(LangConfig.get("Empty.list"), Status.HTTP_NOT_FOUND));
            return false;
        }
        for (Atm atm : atms) {
            if (Objects.nonNull(atm)) {
                Print.println(Color.PURPLE, atm);
            }
        }
        show(new ResponseEntity<String>("----------------", Status.HTTP_OK));
        return true;
    }

    public void update() {
    }
}
