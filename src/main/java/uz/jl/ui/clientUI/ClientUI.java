package uz.jl.ui.clientUI;

import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.enums.card.CardType;
import uz.jl.repository.card.CardRep;
import uz.jl.response.ResponseEntity;
import uz.jl.services.atm.AtmUserService;
import uz.jl.services.auth.AuthUsersServices;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;

import java.util.List;
import java.util.Objects;

import uz.jl.services.atm.AuthUsersService.*;
import static uz.jl.utils.Color.*;
import static uz.jl.utils.Input.getStr;
import static uz.jl.utils.Print.print;
import static uz.jl.utils.Print.println;

/**
 * @author Elmurodov Javohir, Wed 4:50 PM. 12/8/2021
 */
public class ClientUI extends BaseAbstractUI implements BaseUI {

    private static final AuthUsersServices authServices = AuthUsersServices.getInstance();
    private static ClientUI clientUI;

    public static ClientUI getInstance() {
        if (Objects.isNull(clientUI)) {
            return clientUI = new ClientUI();
        }
        return clientUI;
    }

    private ClientUI() {
    }

    @Override
    public void create() {
        String fullname = getStr(LangConfig.get("fulname"));
        String pasportSeria = getStr(LangConfig.get("passport.series"));
        String pasportNumber = getStr(LangConfig.get("passport.number"));
        String username = getStr(LangConfig.get("username"));
        String password = getStr(LangConfig.get("password"));
        String phoneNumber = getStr(LangConfig.get("Phone.number"));
        ResponseEntity<String> response = authServices.creat(fullname, pasportSeria, pasportNumber, username, password, phoneNumber, Role.CLIENT);
        show(response);
    }

    @Override
    public void block() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.block(username, Role.CLIENT);
        show(response);
    }

    @Override
    public void unblock() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.unBlock(username, Role.CLIENT);
        show(response);
    }

    @Override
    public void delete() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.delete(username, Role.CLIENT);
        show(response);
    }

    @Override
    public boolean list() {
        List<AuthUserDto> usersDto = authServices.list(Role.CLIENT);
        if (usersDto.isEmpty()) {
            println(RED, LangConfig.get("Empty.list"));
            return false;
        }
        for (AuthUserDto userDto : usersDto) {
            String color = userDto.getStatus().equals(Status.ACTIVE) ? BLUE : RED;
            String emoje = userDto.getStatus().equals(Status.ACTIVE) ? "🎯" : "🔒";
            println(color, userDto.getUsername() + " \t" + userDto.getPhoneNumber() + " \t" + emoje);
        }
        println(YELLOW, "------------------------");
        return true;
    }

    public void orderCard() {
        ResponseEntity<String> response = authServices.orderCard(Session.getInstance().getUser().getId());
        show(response);
    }

    public void takeCash() {
        switch (getChoise()) {
            case "1" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("50000");
                show(response);
            }
            case "2" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("100000");
                show(response);
            }
            case "3" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("200000");
                show(response);
            }
            case "4" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("300000");
                show(response);
            }
            case "5" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("400000");
                show(response);
            }
            case "6" -> {
                ResponseEntity<String> response = AtmUserService.takeCash("500000");
                show(response);
            }
            case "7" -> {
                String sum = getStr("Summa: ");
                if (sum.length() < 4) {
                    for (int i = 0; i < sum.length(); i++) {
                        if (sum.charAt(i) - '0' < 0 || sum.charAt(i) - '0' > 9) {
                            show(new ResponseEntity<>(LangConfig.get("Bad.Credentials"), uz.jl.response.Status.HTTP_FORBIDDEN));
                            return;
                        }
                    }
                } else {
                    show(new ResponseEntity<>(LangConfig.get("Bad.Credentials"), uz.jl.response.Status.HTTP_FORBIDDEN));
                    return;
                }
                ResponseEntity<String> response = AtmUserService.takeCash(sum);
                show(response);
            }
        }
    }

    private String getChoise() {
        print(YELLOW, "1-> \t 50 000 " + LangConfig.get("sum") + "\t\t\t");
        println(YELLOW, "5-> \t 400 000 " + LangConfig.get("sum"));
        print(YELLOW, "2-> \t 100 000 " + LangConfig.get("sum") + "\t\t\t");
        println(YELLOW, "6-> \t 500 000 " + LangConfig.get("sum"));
        print(YELLOW, "3-> \t 200 000 " + LangConfig.get("sum") + "\t\t\t");
        println(YELLOW, "7-> \t" + LangConfig.get("Another.value"));
        println(YELLOW, "4-> \t 300 000 " + LangConfig.get("sum"));
        String choise = getStr("? -> ");
        return choise;
    }

    public void convertation() {
        ResponseEntity<String> response;
        if (Objects.nonNull(CardRep.sessionCard)) {
            println(YELLOW, "1 USD = 10500 UZS");
            println(PURPLE, "In ATM yes 50$ and 100$ money");
            String price;
            String conversion;
            if (CardRep.sessionCard.getCardType().equals(CardType.VISA)) {
                price = getStr("How many USD do you convert to UZS: ");
                if (checkPrice(price)) return;
                conversion = "USD";
            } else {
                price = getStr("How many USD do you need: ");
                if (checkPrice(price)) return;
                conversion = "UZS";
            }
            response = AtmUserService.convertation(price, conversion);
        } else {
            response = new ResponseEntity<>("Your not Card!", uz.jl.response.Status.HTTP_NOT_FOUND);
        }
        show(response);
    }

    private boolean checkPrice(String price) {
        if (price.length() < 2) {
            show(new ResponseEntity<>(LangConfig.get("Bad.Credentials"), uz.jl.response.Status.HTTP_FORBIDDEN));
            return true;
        } else {
            for (int i = 0; i < price.length(); i++) {
                if (price.charAt(i) - '0' < 0 || price.charAt(i) - '0' > 9) {
                    show(new ResponseEntity<>(LangConfig.get("Bad.Credentials"), uz.jl.response.Status.HTTP_FORBIDDEN));
                    return true;
                }
            }
        }
        int many = Integer.parseInt(price);
        if (many % 50 != 0) {
            show(new ResponseEntity<>(LangConfig.get("Bad.Credentials"), uz.jl.response.Status.HTTP_FORBIDDEN));
            return true;
        }
        return false;
    }


}

