package uz.jl.services.atm.miniServices;

import com.google.gson.reflect.TypeToken;
import uz.jl.configs.LangConfig;
import uz.jl.models.card.Card;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;
import uz.jl.services.history.HistoryService;

import java.lang.reflect.Type;
import java.util.List;

public class Card2CardService {

    private static FileData<Card> fileData = new FileData<>();
    private static final String file = "src/main/resources/db/card.json";
    private static Type type = new TypeToken<List<Card>>() {
    }.getType();
    private static List<Card> cards = fileData.myReader(file, type);


    public static ResponseEntity<String> card2Card(String senderPan, String receiverPan, double sum) {
        for (Card card : cards) {
            if (card.getPan().equals(senderPan)){
                card.setBalance(card.getBalance()-sum);
            }
            if (card.getPan().equals(receiverPan)){
                card.setBalance(card.getBalance()+sum);
            }
        }
        HistoryService.getInstance().writeHistory(senderPan,receiverPan,sum, LangConfig.get("Card.to.Card"));
        fileData.myWriter(cards,file);
        return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
    }
}
