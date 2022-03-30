package uz.jl.services.atm.miniServices;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.models.card.Card;
import uz.jl.models.history.History;
import uz.jl.repository.atm.AtmRepository;
import uz.jl.repository.card.CardRep;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;
import uz.jl.services.history.HistoryService;
import uz.jl.utils.Input;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TakCashService {
    private static TakCashService takCashService;
    private static FileData<Card> fileData = new FileData<>();
    private static FileData<Atm> fileDataA = new FileData<>();
    private static final String file = "src/main/resources/db/card.json";
    private static final String fileA = "src/main/resources/db/atm.json";
    private static Type type = new TypeToken<List<Card>>() {
    }.getType();
    private static Type typeA = new TypeToken<List<Atm>>() {
    }.getType();

    public static ResponseEntity<String> takeCash(String pan, double sum) {
        List<Card> cards = fileData.myReader(file, type);
        List<Atm> atms = fileDataA.myReader(fileA, typeA);
        List<Cassette> cassettes = getUzsTypeCassette();
        ResponseEntity<String> response = checkSum(sum);

        if (Objects.isNull(cassettes)) {
            return new ResponseEntity<>("Type is wrong", Status.HTTP_BAD_REQUEST);
        }
        if (response.getStatus()!=200) {
            return response;
        }

//TODO Cassette dan ayrb ayrb ketuvrishi kerak
//        int counter = 0;
//        double rem = sum;
//        if (rem%100000==0){
//            for (Cassette cassette : cassettes) {
//               if (cassette.getCurrencyValue().getValue()==100000D){
//                   while (sum%100000<=0){
//
//                       cassettes++;
//
//                   }
//               }
//            }
//        }
//
//        for (Atm atm : atms) {
//            if (atm.getLocation().equals(AtmRepository.sessionAtm.getLocation())){
//
//            }
//        }

        for (Card card : cards) {
            if (card.getPan().equals(pan)) {
                card.setBalance(card.getBalance() - (sum*(sum*1.01)));
                HistoryService.getInstance().writeHistory(pan, (sum*(sum*1.01)), "Cash Taken");
                fileData.myWriter(cards, file);
                return new ResponseEntity<>("Success", Status.HTTP_OK);
            }
        }
        return new ResponseEntity<>("Xatolik", Status.HTTP_BAD_REQUEST);
    }

    private static ResponseEntity<String> checkSum(double sum) {
        double cassetteBalance = 0;
        for (Cassette cassette : Objects.requireNonNull(getUzsTypeCassette())) {
            cassetteBalance += cassette.getBalance();
        }
        if (!(sum <= cassetteBalance)) return new ResponseEntity<>("Cassettes are not working", Status.HTTP_FORBIDDEN);
        if (!((sum + (sum * 1.01)) <= CardRep.sessionCard.getBalance()))
            return new ResponseEntity<>("Card does not have enough money", Status.HTTP_FORBIDDEN);
        return new ResponseEntity<>("\n", Status.HTTP_OK);
    }

    private static List<Cassette> getUzsTypeCassette() {
        List<Cassette> cassettes = new ArrayList<>();
        for (Cassette cassette : AtmRepository.sessionAtm.getCassette()) {
            if (cassette.getCassetteType().toString().equalsIgnoreCase("uzs")) {
                cassettes.add(cassette);
            }
        }
        if (cassettes.isEmpty()) {
            return null;
        }
        return cassettes;
    }

    public ResponseEntity<String> takeCashFunction() {
        FileData<Atm> fileData = new FileData<>();
        final String file = "src/main/resources/db/atm.json";
        Type type = new TypeToken<List<Atm>>() {
        }.getType();
        List<Atm> atms = fileData.myReader(file, type);
        int i = 1;
        for (Atm atm : atms) {
            System.out.println("#" + (i++) + atm.getLocation() + "\n");
        }
        String loc = Input.getStr("Enter  location: ");
        Atm atm = AtmRepository.getInstance().findByLocation(loc);
        if (Objects.isNull(atm)) {
            return new ResponseEntity<>("Atm wrong ", Status.HTTP_NOT_FOUND);
        }
        AtmRepository.sessionAtm = atm;
        return new ResponseEntity<>("Welcome", Status.HTTP_OK);
    }

    private TakCashService() {
    }

    public static TakCashService getInstance() {
        if (takCashService == null) {
            return takCashService = new TakCashService();
        }
        return takCashService;
    }
}
