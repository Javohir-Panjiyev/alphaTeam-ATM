package uz.jl.ui.authUI;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserProfileDto;
import uz.jl.dtos.auth.CardDto;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.auth.AuthUserService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.Objects;

import static uz.jl.services.auth.AuthUsersServices.changeLanguage;
import static uz.jl.utils.Color.*;
import static uz.jl.utils.Input.getStr;
import static uz.jl.utils.Print.print;
import static uz.jl.utils.Print.println;

/**
 * @author Elmurodov Javohir, Wed 4:52 PM. 12/8/2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthUI extends BaseAbstractUI /*implements BaseUI*/ {
    private AuthUserService authUserService = AuthUserService.getInstance();
    private static AuthUI authUI;

    public static AuthUI getInstance() {
        if (Objects.isNull(authUI)) {
            return authUI = new AuthUI();
        }
        return authUI;
    }


    public void login() {
        String username = Input.getStr(LangConfig.get("username"));
        String password = Input.getStr(LangConfig.get("password"));
        ResponseEntity<String> response = authUserService.login(username, password);
        show(response);
    }

    public void logout() {
        ResponseEntity<String> response = authUserService.logout();
        show(response);
    }

    public void profile() {
        AuthUserProfileDto usersProfileDto = authUserService.profile(Session.getInstance().getUser().getId());
        println(YELLOW, LangConfig.get("username") + usersProfileDto.getUsername() + "\t"+LangConfig.get("password") + usersProfileDto.getPassword() + "\t"+LangConfig.get("Phone.number") + usersProfileDto.getPhoneNumber());
        if (usersProfileDto.getCards().size() != 0) {
            for (CardDto card : usersProfileDto.getCards()) {
                println(PURPLE, card.getCardType() + " : " + card.getPan() + " \t" + card.getPassword());
                println(PURPLE, "\t\t\t\t" + card.getExpiry());
                println(PURPLE, LangConfig.get("Balance") + card.getBalance());
                println(PURPLE, "  ------------------------");
            }
        } else {
            println(PURPLE, LangConfig.get("You.have.not.cards"));
        }
    }

    public void change(){
        println(PURPLE,"UZ");
        println(PURPLE,"EN");
        println(PURPLE,"RU");
        String lan = getStr("?..");
        if(lan.equals("uz")) changeLanguage(lan);
        else if(lan.equals("en")) changeLanguage(lan);
        else if(lan.equals("ru")) changeLanguage(lan);
        else  Print.println(Color.RED, LangConfig.get("Wrong.Choice"));

    }

    public void quit() {
       println(RED,LangConfig.get("Come.again"));
    }
}
