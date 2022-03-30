package uz.jl.services.card;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.RandomStringUtils;
import uz.jl.configs.LangConfig;
import uz.jl.enums.card.CardType;
import uz.jl.models.card.Card;
import uz.jl.models.personal.Passport;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.filesystems.FileData;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static uz.jl.utils.Color.*;
import static uz.jl.utils.Print.print;
import static uz.jl.utils.Print.println;

/**
 * @author Narzullayev Husan, Fri 11:53 AM. 12/10/2021
 */
public class CardService {

    private static final Type typePP = new TypeToken<List<Passport>>() {
    }.getType();

    private static final Card card = new Card();
    private static final FileData<Card> fileData = new FileData<>();
    private static final String file = "src/main/resources/db/card.json";
    private static final Type type = new TypeToken<List<Card>>() {
    }.getType();
    private static List<Card> cards;

    private static final FileData<Passport> fileDataP = new FileData<>();
    private static final String fileP = "src/main/resources/db/requests.json";
    private static final Type typeP = new TypeToken<List<Passport>>() {
    }.getType();

    public static ResponseEntity<String> create(String passwordSeria, String passwordNumber, String cardType) {
        cards = fileData.myReader(file, type);

        if (!(checkPassport(passwordSeria, passwordNumber))) {
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
        }

        for (CardType value : CardType.values()) {
            if (value.getCode().equals(cardType)) {
                String pan = cardType + panGenerition();
                card.setPan(pan);
                card.setCardType(value);
            }
        }

        if (Objects.isNull(card.getPan())) {
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
        }
        List<Passport> passports = fileDataP.myReader(fileP, typeP);
        for (Passport passport : passports) {
            if (passport.getNumber().equals(passwordNumber)) {
                card.setCardStatus(uz.jl.enums.Status.ACTIVE);
                card.setExpiry(dateCreator());
                card.setBalance(0D);
                card.setPassword("7777");
                card.setOwnerId(passport.getOwnerId());
                cards.add(card);
                fileData.myWriter(cards, file);

                passports.remove(passport);
                fileDataP.myWriter(passports, fileP);
                return new ResponseEntity<>(LangConfig.get("Successfully"));
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    private static Boolean checkPassport(String passwordSeria, String passwordNumber) {
        for (Passport passport : requestList()) {
            if (passport.getSerial().equals(passwordSeria) && passport.getNumber().equals(passwordNumber)) {
                return true;
            }
        }
        return false;
    }

    public static List<Passport> requestList() {
        FileData<Passport> fileData = new FileData<>();
        Type type = new TypeToken<List<Passport>>() {
        }.getType();
        String reqFile = "src/main/resources/db/requests.json";
        return fileData.myReader(reqFile, type);
    }

    public static ResponseEntity<String> block(String pan, ResponseEntity list) {
        if (checkCards(pan).equals(Status.HTTP_OK)) {
            if (list.equals(Status.HTTP_OK)) {
                for (Card card1 : cards) {
                    if (card1.getPan().equals(pan)) {
                        card1.setCardStatus(uz.jl.enums.Status.BLOCKED);
                        return new ResponseEntity<>(LangConfig.get("Successfully.blocked"), Status.HTTP_OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> unblock() {
        cards = fileData.myReader(file, type);
        for (Card card1 : cards) {
            if (card1.getCardStatus().equals(uz.jl.enums.Status.BLOCKED)) {
                println(RED, card1);
            }
        }
        String pan = Input.getStr(LangConfig.get("Choice.the.card.number"));
        if (checkCards(pan).equals(Status.HTTP_OK)) {
            for (Card card1 : cards) {
                if (card1.getCardStatus().equals(uz.jl.enums.Status.BLOCKED)) {
                    if (Objects.equals(card1.getPan(), card)) {
                        card1.setCardStatus(uz.jl.enums.Status.BLOCKED);
                        return new ResponseEntity<>(LangConfig.get("Successfully.unblocked"), Status.HTTP_OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> delete(String pan, ResponseEntity list) {
        if (checkCards(pan).equals(Status.HTTP_OK)) {
            if (list.equals(Status.HTTP_OK)) {
                for (Card card1 : cards) {
                    if (card1.getPan().equals(pan)) {
                        cards.remove(card1);
                        return new ResponseEntity<>(LangConfig.get("Successfully.deleted"), Status.HTTP_OK);
                    }
                }
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> list() {
        cards = fileData.myReader(file, type);
        if (cards.isEmpty()) {
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
        } else {
            for (Card card1 : cards) {
                if (card1.getCardStatus().equals(uz.jl.enums.Status.ACTIVE)) {

                    photoCard(GREEN, card1);

                } else {
                    photoCard(RED, card1);
                }
            }
        }
        return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
    }

    private static void photoCard(String green, Card card) {


        println(green, "====================================");
          print(green, "-- ") ; println(BLUE,card.getCardType());
          print(green, "--");print(YELLOW,"*******");println(green,"                         --");
          print(green, "--");print(YELLOW,"*******");println(green,"                         --");
          print(green, "--");print(YELLOW,"*******");println(green,"                         --");
        print(green, "-- ") ; println(BLUE, card.getPan());
        print(green, "--              " ) ; println(BLUE, card.getExpiry());
        print(green, "-- ") ; println(PURPLE, getUsername(card.getOwnerId()));
        println(green, "====================================");
        println("");

    }

    public static ResponseEntity<String> uptade(String card, ResponseEntity list) {
        if (checkCards(card).equals(Status.HTTP_FORBIDDEN)) {
            if (list.equals(Status.HTTP_OK)) {
                for (Card card1 : cards) {
                    if (card1.getPan().equals(card)) {
                        println(LangConfig.get("Enter.the.old.password"));
                        String oldPassword = Input.getStr("-> ");
                        println(LangConfig.get("Enter.the.new.password"));
                        String newPassword = Input.getStr("-> ");
                        println(LangConfig.get("Re.enter.the.new.password"));
                        String new1Password = Input.getStr("-> ");
                        if (card1.getPassword().equals(oldPassword)) {
                            if (newPassword.equals(new1Password)) {
                                for (int i = 0; i < 4; i++) {
                                    if (!(newPassword.charAt(i) >= 48 && newPassword.charAt(i) <= 57)) {
                                        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
                                    }
                                }
                                cards = fileData.myReader(file, type);
                                card1.setPassword(newPassword);
                                fileData.myWriter(cards, file);
                                return new ResponseEntity<>(LangConfig.get("Password.successfully.updated"), Status.HTTP_OK);
                            }

                        }
                    }
                }
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public static ResponseEntity<String> uptade(Card card) {
        println(LangConfig.get("Enter.the.old.password"));
        String oldPassword = Input.getStr("-> ");
        println(LangConfig.get("Enter.the.new.password"));
        String newPassword = Input.getStr("-> ");
        println(LangConfig.get("Re.enter.the.new.password"));
        String new1Password = Input.getStr("-> ");
        if (card.getPassword().equals(oldPassword)) {
            if (newPassword.equals(new1Password)) {
                for (int i = 0; i < 4; i++) {
                    if (!(newPassword.charAt(i) >= 48 && newPassword.charAt(i) <= 57)) {
                        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
                    }
                }
                cards = fileData.myReader(file, type);
                card.setPassword(newPassword);
                fileData.myWriter(cards, file);
                return new ResponseEntity<>(LangConfig.get("Password.successfully.updated"), Status.HTTP_OK);
            }

        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }


    public static ResponseEntity<String> checkCards(String pan) {
        for (Card card1 : cards) {
            if (card1.getPan().equals(pan)) {
                return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
            }
        }
        return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
    }


    public static ResponseEntity<String> checkPan(String pan, String password) {
        for (int i = 4; i < pan.length(); i++) {
            if (!(pan.charAt(i) >= 48 && pan.charAt(i) <= 57)) {
                return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
            }
        }
        for (int i = 0; i < 4; i++) {
            if (!(password.charAt(i) >= 48 && password.charAt(i) <= 57)) {
                return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
            }
        }
        return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
    }

    private static String panGenerition() {
        return RandomStringUtils.random(12, false, true);
    }

    private static String dateCreator() {
        LocalDate localDate = LocalDate.now();
        localDate.format(DateTimeFormatter.ofPattern("MM/yy"));
        LocalDate expiry = localDate.plusYears(5);
        return expiry.format(DateTimeFormatter.ofPattern("MM/yy"));
    }

    private static String getUsername(String id) {

        String filePP = "src/main/resources/db/pasports.json";



        List<Passport> passports = fileDataP.myReader(filePP, typePP);
        for (Passport passport : passports) {
            if (passport.getOwnerId().equals(id)) {
                return passport.getFullName();
            }
        }
        return "#";
    }

}

