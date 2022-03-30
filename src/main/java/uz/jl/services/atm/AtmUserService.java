package uz.jl.services.atm;

import com.google.gson.reflect.TypeToken;
import uz.jl.configs.LangConfig;
import uz.jl.enums.CassetteType;
import uz.jl.enums.Money;
import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.atm.Atm;
import uz.jl.models.atm.Cassette;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.repository.card.CardRep;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static uz.jl.repository.atm.AtmRepository.sessionAtm;

/**
 * @author Narzullayev Husan, Sat 2:51 PM. 12/11/2021
 */
public class AtmUserService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {

    public static AtmUserService atmUserService;

    protected AtmUserService(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public ResponseEntity<List<AuthUser>> getAll() {
        List<AuthUser> users = new ArrayList<>();
        return new ResponseEntity<>(users, Status.HTTP_NOT_FOUND);
    }


    @Override
    public ResponseEntity<AuthUser> get(String id) {
        return new ResponseEntity<>(new AuthUser());
    }


    public static ResponseEntity<String> balance() {
        if (Objects.nonNull(CardRep.sessionCard)) {
            return new ResponseEntity<>("Your balance: " + CardRep.sessionCard.getBalance());
        }
        return new ResponseEntity<>("UserCard not found", Status.HTTP_NOT_FOUND);
    }


    public static ResponseEntity<String> operationService() {

        return null;
    }

    public static ResponseEntity<String> takeCash(String amount) {
        return null;
    }

    public static void history() {

    }

    public static ResponseEntity<String> convertation(String price, String typeMony) {
        double converPrice = Double.parseDouble(price);
        FileData<Card> fileData = new FileData<>();
        String path = "src/main/resources/db/card.json";
        Type type = new TypeToken<List<Card>>() {
        }.getType();
        List<Card> cards;
        cards = fileData.myReader(path, type);
        cards.remove(CardRep.sessionCard);

        FileData<Atm> fileData1 = new FileData<>();
        String file1 = "src/main/resources/db/atm.json";
        Type type1 = new TypeToken<List<Atm>>() {}.getType();
        List<Atm> atms;
        atms = fileData1.myReader(file1, type1);
        atms.remove(sessionAtm);

        if (CardRep.sessionCard.getBalance() >= converPrice) {
            if (typeMony.equals("UZS")) {
                ResponseEntity<String> HTTP_FORBIDDEN = getResponseEntity(converPrice);
                if (HTTP_FORBIDDEN != null) return HTTP_FORBIDDEN;
            }
            else {
                ResponseEntity<String> HTTP_FORBIDDEN = getStringResponseEntity(converPrice);
                if (HTTP_FORBIDDEN != null) return HTTP_FORBIDDEN;
            }
        } else {
            return new ResponseEntity<>(LangConfig.get("Card not price!"), Status.HTTP_FORBIDDEN);
        }
        cards.add(CardRep.sessionCard);
        fileData.myWriter(cards, path);

        atms.add(sessionAtm);
        fileData1.myWriter(atms, file1);
        return new ResponseEntity<>(LangConfig.get("Succissfully"), Status.HTTP_OK);
    }

    private static ResponseEntity<String> getResponseEntity(double converPrice) {
        double atmPrice = 0;
        int countAtm100 = 0;
        int countAtm50 = 0;
        for (Cassette cassette : sessionAtm.getCassette()) {
            if (cassette.getCassetteType().equals(CassetteType.USD)) {
                if (cassette.getCurrencyValue().equals(Money.YUZ_DOLLAR)) {
                    countAtm100 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm100 * 100.0;
                } else if (cassette.getCurrencyValue().equals(Money.ELLIK_DOLLAR)) {
                    countAtm50 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm50 * 50.0;
                }
            }
        }
        if (atmPrice >= converPrice) {
            CardRep.sessionCard.setBalance(CardRep.sessionCard.getBalance() - converPrice);
            int count100 = (int) (converPrice / 100);
            int count50 = (int) (converPrice % 100) != 0 ? 1 : 0;
            if (count50 > 0 && countAtm50 == 0)
                return new ResponseEntity<>("Bad credentials", Status.HTTP_FORBIDDEN);
            int check100 = countAtm100 - count100;
            extraCheckUSD(countAtm100, count100, count50, check100);
        } else {
            return new ResponseEntity<>(LangConfig.get("Atm not price!"), Status.HTTP_FORBIDDEN);
        }
        return null;
    }

    private static void extraCheckUSD(int countAtm100, int count100, int count50, int check100) {
        if (check100 > 0) {
            for (Cassette cassette : sessionAtm.getCassette()) {
                if (cassette.getCurrencyValue().equals(Money.YUZ_DOLLAR)) {
                    cassette.setCurrencyCount(String.valueOf(Integer.parseInt(cassette.getCurrencyCount()) - count100));
                } else if (cassette.getCurrencyValue().equals(Money.ELLIK_DOLLAR)) {
                    cassette.setCurrencyCount(String.valueOf(Integer.parseInt(cassette.getCurrencyCount()) - count50));
                }
            }
        } else {
            for (Cassette cassette : sessionAtm.getCassette()) {
                if (cassette.getCurrencyValue().equals(Money.YUZ_DOLLAR)) {
                    cassette.setCurrencyCount(String.valueOf(Integer.parseInt(cassette.getCurrencyCount()) - count100));
                } else if (cassette.getCurrencyValue().equals(Money.ELLIK_DOLLAR)) {
                    count50 += 2 * (count100 - countAtm100);
                    cassette.setCurrencyCount(String.valueOf(Integer.parseInt(cassette.getCurrencyCount()) - count50));
                }
            }
        }
    }

    private static ResponseEntity<String> getStringResponseEntity(double converPrice) {
        double uzsPrice = converPrice * 10500;
        double atmPrice = 0;
        int countAtm5;
        int countAtm10;
        int countAtm20;
        int countAtm50;
        int countAtm100;
        for (Cassette cassette : sessionAtm.getCassette()) {
            if (cassette.getCassetteType().equals(CassetteType.UZS)) {
                if (cassette.getCurrencyValue().equals(Money.BESH_MING_SUM)) {
                    countAtm5 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm5 * 5000;
                } else if (cassette.getCurrencyValue().equals(Money.ON_MING_SUM)) {
                    countAtm10 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm10 * 10000;
                } else if (cassette.getCurrencyValue().equals(Money.YIGIRMA_MING_SUM)) {
                    countAtm20 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm20 * 20000;
                } else if (cassette.getCurrencyValue().equals(Money.ELLIK_MING_SUM)) {
                    countAtm50 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm50 * 50000;
                } else if (cassette.getCurrencyValue().equals(Money.YUZ_MING_SUM)) {
                    countAtm100 = Integer.parseInt(cassette.getCurrencyCount());
                    atmPrice += countAtm100 * 100000;
                }
            }
        }
        ResponseEntity<String> HTTP_FORBIDDEN = getStringResponseEntity(uzsPrice, atmPrice);
        if (HTTP_FORBIDDEN != null) return HTTP_FORBIDDEN;
        return null;
    }

    private static ResponseEntity<String> getStringResponseEntity(double uzsPrice, double atmPrice) {
        if (uzsPrice <= atmPrice) {
            int count100 = (int) (uzsPrice / 100);
            int count50 = (int) ((uzsPrice % 100) / 50);
            int count20 = (int) (((uzsPrice % 100) % 50) / 20);
            int count10 = (int) ((((uzsPrice % 100) % 50) % 20) / 10);
            int count5 = (int) (uzsPrice % 5) != 0 ? 1 : 0;

            extraUZS(count100, count50, count20, count10, count5);
        }
        else {
            return new ResponseEntity<>(LangConfig.get("Card not price!"), Status.HTTP_FORBIDDEN);
        }
        return null;
    }

    private static void extraUZS(int count100, int count50, int count20, int count10, int count5) {
        for (Cassette cassette : sessionAtm.getCassette()) {
            if (cassette.getCurrencyValue().equals(Money.YUZ_MING_SUM)) {
                Integer count = Integer.parseInt(cassette.getCurrencyCount()) - count100;
                if (count >= 0) {
                    cassette.setCurrencyCount(String.valueOf(count));
                } else {
                    count50 += Math.abs(count);
                    cassette.setCurrencyCount("0");
                }
            } else if (cassette.getCurrencyValue().equals(Money.ELLIK_MING_SUM)) {
                Integer count = Integer.parseInt(cassette.getCurrencyCount()) - count50;
                if (count >= 0) {
                    cassette.setCurrencyCount(String.valueOf(count));
                } else {
                    count20 += Math.abs(count);
                    cassette.setCurrencyCount("0");
                }
            } else if (cassette.getCurrencyValue().equals(Money.YIGIRMA_MING_SUM)) {
                Integer count = Integer.parseInt(cassette.getCurrencyCount()) - count20;
                if (count >= 0) {
                    cassette.setCurrencyCount(String.valueOf(count));
                } else {
                    count10 += Math.abs(count);
                    cassette.setCurrencyCount("0");
                }
            } else if (cassette.getCurrencyValue().equals(Money.ON_MING_SUM)) {
                Integer count = Integer.parseInt(cassette.getCurrencyCount()) - count10;
                if (count >= 0) {
                    cassette.setCurrencyCount(String.valueOf(count));
                } else {
                    count5 += Math.abs(count);
                    cassette.setCurrencyCount("0");
                }
            } else if (cassette.getCurrencyValue().equals(Money.BESH_MING_SUM)) {
                Integer count = Integer.parseInt(cassette.getCurrencyCount()) - count5;
                cassette.setCurrencyCount(String.valueOf(count));
            }
        }
    }

    public static ResponseEntity<String> services(String pan, String password) {
        Card card = CardRep.findByPan(pan);
        if (Objects.isNull(card)) {
            return new ResponseEntity<>("Bad credentials", Status.HTTP_FORBIDDEN);
        }
        if (!card.getPassword().equals(password)) {
            return new ResponseEntity<>("Bad credentials", Status.HTTP_FORBIDDEN);
        }
        CardRep.sessionCard = card;
        return new ResponseEntity<>("Success", Status.HTTP_OK);
    }
}
