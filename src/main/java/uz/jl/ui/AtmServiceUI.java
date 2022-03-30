package uz.jl.ui;

import uz.jl.repository.card.CardRep;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.atm.AtmUserService;
import uz.jl.services.atm.miniServices.Card2CardService;
import uz.jl.services.atm.miniServices.Convertion;
import uz.jl.services.atm.miniServices.TakCashService;
import uz.jl.services.history.HistoryService;
import uz.jl.utils.Input;

/**
 * @author Juraev Nodirbek, сб 16:52. 11.12.2021
 */
public class AtmServiceUI extends BaseAbstractUI {
    private static AtmServiceUI atmServiceUI;

    private AtmServiceUI() {
    }

    public static AtmServiceUI getInstance() {
        if (atmServiceUI == null) {
            atmServiceUI = new AtmServiceUI();
        }
        return atmServiceUI;
    }


    public void services() {
        ResponseEntity<String> response = TakCashService.getInstance().takeCashFunction();
        if (response.getStatus()!=200) {
            show(response);
            return;
        }
            show(response);
        String pan = Input.getStr("Enter pan of a card: ");
        String password = Input.getStr("Enter PIN code: ");
        response = AtmUserService.services(pan, password);
        show(response);
    }


    public void balance() {
        ResponseEntity<String> response = AtmUserService.balance();
        show(response);
    }

    public void takeCash() {
        int summa = Input.getNum("Enter amount: ");
        ResponseEntity<String> response = TakCashService.getInstance().takeCash(CardRep.sessionCard.getPan(), summa);
        show(response);
    }

    public void card2Card() {
        String receiverPan = Input.getStr("Enter receiver card pan: ");
        double summa = Input.getNum("Enter amount: ");
        show(new ResponseEntity<>(Card2CardService.card2Card(CardRep.sessionCard.getPan(), receiverPan, summa)));
    }

    public void operations() {
//TODO
    }

    public void history() {
//TODO
        HistoryService.getInstance().findHistory(CardRep.sessionCard.getPan());
    }

    public void conversation() {
//TODO
        String type = Input.getStr("Dollar_sotish | Dollar sotib olish");
        double sum = Input.getNum("Summani kiriting : ");
        show(new ResponseEntity<>(Convertion.convert(sum, type)));

    }
}
