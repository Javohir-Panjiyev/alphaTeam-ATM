package uz.jl.ui;

import uz.jl.configs.LangConfig;
import com.google.gson.reflect.TypeToken;
import uz.jl.models.personal.Passport;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.card.CardService;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.Locale;

import static uz.jl.utils.Color.YELLOW;
import static uz.jl.utils.Input.getStr;

/**
 * @author Narzullayev Husan, Fri 11:52 AM. 12/10/2021
 */
public class CardUI extends BaseAbstractUI implements BaseUI {

    @Override
    public void create() {
        if (CardService.requestList().isEmpty()) {
            ResponseEntity<String> response = new ResponseEntity<>(LangConfig.get("Card.List.empty"), Status.HTTP_FORBIDDEN);
            show(response);
            return;
        }
        for (Passport passport : CardService.requestList()) {
            Print.println(YELLOW, passport.getFullName() + "\t" + passport.getSerial() + "  " + passport.getNumber());
        }
        String pasportSeria = getStr(LangConfig.get("passport.series")).toUpperCase(Locale.ROOT);
        String pasportNumber = getStr(LangConfig.get("passport.number"));
        Print.println(LangConfig.get("Enter.the.card.type")+" (8600/9860/6320/4790/4567");
        String cardType = Input.getStr(" -> : ");
        ResponseEntity<String> response = CardService.create(pasportSeria, pasportNumber, cardType);
        show(response);
    }

    @Override
    public void block() {
        ResponseEntity<String> list = CardService.list();
        String  card=Input.getStr(LangConfig.get("Choice.the.card.number"));
        ResponseEntity<String> response = CardService.block(card, list);
        show(response);
    }

    @Override
    public void unblock() {

    }

    @Override
    public void delete() {
        ResponseEntity<String> list = CardService.list();
        String  card=Input.getStr(LangConfig.get("Choice.the.card.number"));

        ResponseEntity<String> response = CardService.delete(card, list);
        show(response);

    }

    @Override
    public boolean list() {
        return false;
    }

    public void uptade() {
        ResponseEntity<String> list = CardService.list();
        String  card=Input.getStr(LangConfig.get("Choice.the.card.number"));
        ResponseEntity<String> response = CardService.uptade(card, list);
        show(response);
    }

private static CardUI instance;

    private CardUI() {
    }

    public static CardUI getInstance(){
        if (instance==null){
            instance = new CardUI();
        }
        return instance;
    }

}
