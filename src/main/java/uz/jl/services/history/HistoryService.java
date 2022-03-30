package uz.jl.services.history;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
import uz.jl.models.history.History;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryService {
    private static HistoryService historyService;

    private static FileData<History> fileData = new FileData<>();
    private static FileData<Card> cardFile = new FileData<>();
    private static FileData<AuthUser> userFile = new FileData<>();

    public List<History> findHistory(String cardPan) {
        final String file = "src/main/resources/db/history.json";
        Type typeHistoryList = new TypeToken<List<History>>() {
        }.getType();

        List<History> histories = fileData.myReader(file, typeHistoryList);
        List<History> returnHistories = new ArrayList<>();

        for (History h : histories) {
            if (h.getSenderPan().equals(cardPan)) {
                returnHistories.add(h);
            }
        }
        return returnHistories;
    }

    public void writeHistory(String senderPan, String receiverPan, double summa, String description) {
        String file = "src/main/resources/db/history.json";
        Type typeHistoryList = new TypeToken<List<History>>() {
        }.getType();
        Card senderCard = getCard(senderPan);
        Card receiverCard = getCard(receiverPan);
        AuthUser senderUser = getUser(senderCard.getOwnerId());
        AuthUser receiverUser = getUser(receiverCard.getOwnerId());


        History history = new History();
        history.setCardType(senderCard.getCardType());
        history.setSenderName(senderUser.getUsername());
        history.setReceiverName(receiverUser.getUsername());
        history.setBalance(senderCard.getBalance());
        history.setSumma(summa);
        history.setReceiverPan(receiverPan);
        history.setSenderPan(senderPan);
        history.setDescription(description);

        List<History> histories = fileData.myReader(file, typeHistoryList);
        histories.add(history);
        fileData.myWriter(histories, file);


    }

    public void writeHistory(String senderPan, double summa, String description) {
        String file = "src/main/resources/db/history.json";
        Type typeHistoryList = new TypeToken<List<History>>() {
        }.getType();
        Card senderCard = getCard(senderPan);
        AuthUser senderUser = getUser(senderCard.getOwnerId());


        History history = new History();
        history.setCardType(senderCard.getCardType());
        history.setSenderName(senderUser.getUsername());

        history.setBalance(senderCard.getBalance());
        history.setSumma(summa);

        history.setSenderPan(senderPan);
        history.setDescription(description);

        List<History> histories = fileData.myReader(file, typeHistoryList);
        histories.add(history);
        fileData.myWriter(histories, file);


    }


    private Card getCard(String pan) {
        String file = "src/main/resources/db/card.json";
        Type typeCard = new TypeToken<List<Card>>() {
        }.getType();
        List<Card> cards = cardFile.myReader(file, typeCard);
        for (Card card : cards) {
            if (card.getPan().equals(pan)) {
                return card;
            }
        }
        return new Card();
    }

    private AuthUser getUser(String id) {
        String file = "src/main/resources/db/users.json";
        Type typeUser = new TypeToken<List<AuthUser>>() {
        }.getType();
        List<AuthUser> users = userFile.myReader(file, typeUser);
        for (AuthUser user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return new AuthUser();
    }


    private HistoryService() {
    }

    public static HistoryService getInstance() {
        if (historyService == null) {
            return historyService = new HistoryService();
        }
        return historyService;
    }


}

