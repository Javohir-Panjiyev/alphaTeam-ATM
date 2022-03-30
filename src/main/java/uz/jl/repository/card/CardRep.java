package uz.jl.repository.card;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.card.Card;
import uz.jl.repository.BaseRepository;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Juraev Nodirbek, пн 14:07. 13.12.2021
 */
public class CardRep extends BaseRepository<Card> {
    public static Card  sessionCard;
    private static FileData<Card> fileData = new FileData<>();
    private static String path = "src/main/resources/db/card.json";
    private static Type type = new TypeToken<List<Card>>() {
    }.getType();
    public static List<Card> cards;

    @Override
    protected List<Card> getAll() {
        cards = fileData.myReader(path, type);
        return cards;
    }

    public static Card findByPan(String pan) {
        List<Card> cardes = fileData.myReader(path, type);
        for (Card card : cardes) {
            if (card.getPan().equals(pan))
                return card;
        }
        return null;
    }
}
