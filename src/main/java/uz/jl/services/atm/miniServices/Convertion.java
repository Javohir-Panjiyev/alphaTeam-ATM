package uz.jl.services.atm.miniServices;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.card.Card;
import uz.jl.repository.card.CardRep;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.List;

public class Convertion {
    private static FileData<Card> fileData = new FileData<>();
    private static final String file = "src/main/resources/db/card.json";
    private static Type type = new TypeToken<List<Card>>() {
    }.getType();
    private static List<Card> cards = fileData.myReader(file, type);

    public static ResponseEntity<String> convert(double sum, String type) {
        for (Card card : cards) {
            if (card.getPan().equals(CardRep.sessionCard.getPan())) {
                if (type.equalsIgnoreCase("Dollar_sotish")) {
                    card.setBalance(CardRep.sessionCard.getBalance() + (sum * 10750.00));
                    fileData.myWriter(cards, file);
                    return new ResponseEntity<>("Successfully convert", Status.HTTP_OK);
                } else if (type.equalsIgnoreCase("Dollar_sotib_olish")) {
                    if (CardRep.sessionCard.getBalance() - (sum * 10800.00) < 0) {
                        return new ResponseEntity<>("Bad.Credentials (balansingizda yetarli mablag' mavjud emas)", Status.HTTP_OK);
                    }
                    card.setBalance(CardRep.sessionCard.getBalance() - (sum * 10800.00));
                    fileData.myWriter(cards, file);
                    return new ResponseEntity<>("Successfully convert", Status.HTTP_OK);
                }
            }
        }
        fileData.myWriter(cards, file);
        return new ResponseEntity<>("Bad.Credentials", Status.HTTP_BAD_REQUEST);
    }

}
