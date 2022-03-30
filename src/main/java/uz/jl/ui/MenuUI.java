package uz.jl.ui;

import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.enums.auth.Role;
import uz.jl.enums.settings.Language;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.card.CardRep;
import uz.jl.ui.components.MenuVal;
import uz.jl.utils.Color;
import uz.jl.utils.Print;

import java.util.*;

/**
 * @author Elmurodov Javohir, Wed 3:24 PM. 12/8/2021
 */
public class MenuUI {

    private static Map<String, String> menus() {
        LinkedHashMap<String, String> menus = new LinkedHashMap<>();
        AuthUser user = Session.getInstance().getUser();
        Role role = user.getRole();
        Language language = user.getLanguage();

        if (Role.SUPER_ADMIN.equals(role)) {
//            menus.put(LangConfig.get("branch.create"), MenuVal.BRANCH_CREATE);
//            menus.put(LangConfig.get("branch.delete"), MenuVal.BRANCH_DELETE);
//            menus.put(LangConfig.get("branch.block"), MenuVal.BRANCH_BLOCK);
//            menus.put(LangConfig.get("branch.unblock"), MenuVal.BRANCH_UNBLOCK);
//            menus.put(LangConfig.get("branch.list"), MenuVal.BRANCH_LIST);

            menus.put(LangConfig.get("admin.create"), MenuVal.ADMIN_CREATE);
            menus.put(LangConfig.get("admin.block"), MenuVal.ADMIN_BLOCK);
            menus.put(LangConfig.get("admin.unblock"), MenuVal.ADMIN_UNBLOCK);
            menus.put(LangConfig.get("admin.delete"), MenuVal.ADMIN_DELETE);
            menus.put(LangConfig.get("admin.list"), MenuVal.ADMIN_LIST);
        } else if (Role.ADMIN.equals(role)) {
            menus.put(LangConfig.get("hr.create"), MenuVal.HR_CREATE);
            menus.put(LangConfig.get("hr.block"), MenuVal.HR_BLOCK);
            menus.put(LangConfig.get("hr.unblock"), MenuVal.HR_UNBLOCK);
            menus.put(LangConfig.get("hr.delete"), MenuVal.HR_DELETE);
            menus.put(LangConfig.get("hr.list"), MenuVal.HR_LIST);

            menus.put(LangConfig.get("atm.create"), MenuVal.ATM_CREATE);
            menus.put(LangConfig.get("atm.block"), MenuVal.ATM_BLOCK);
            menus.put(LangConfig.get("atm.unblock"), MenuVal.ATM_UNBLOCK);
            menus.put(LangConfig.get("atm.delete"), MenuVal.ATM_DELETE);
            menus.put(LangConfig.get("atm.list"), MenuVal.ATM_LIST);
        } else if (Role.HR.equals(role)) {
            menus.put(LangConfig.get("employee.create"), MenuVal.EMPLOYEE_CREATE);
            menus.put(LangConfig.get("employee.block"), MenuVal.EMPLOYEE_BLOCK);
            menus.put(LangConfig.get("employee.unblock"), MenuVal.EMPLOYEE_UNBLOCK);
            menus.put(LangConfig.get("employee.delete"), MenuVal.EMPLOYEE_DELETE);
            menus.put(LangConfig.get("employee.list"), MenuVal.EMPLOYEE_LIST);
        } else if (Role.EMPLOYEE.equals(role)) {
            menus.put(LangConfig.get("client.create"), MenuVal.CLIENT_CREATE);
            menus.put(LangConfig.get("client.block"), MenuVal.CLIENT_BLOCK);
            menus.put(LangConfig.get("client.unblock"), MenuVal.CLIENT_UNBLOCK);
            menus.put(LangConfig.get("client.delete"), MenuVal.CLIENT_DELETE);
            menus.put(LangConfig.get("client.list"), MenuVal.CLIENT_LIST);

            menus.put(LangConfig.get("card.create"), MenuVal.CARD_CREATE);
            menus.put(LangConfig.get("card.block"), MenuVal.CARD_BLOCK);
            menus.put(LangConfig.get("card.unblock"), MenuVal.CARD_UNBLOCK);
            menus.put(LangConfig.get("card.delete"), MenuVal.CARD_DELETE);
            menus.put(LangConfig.get("card.update"), MenuVal.CARD_UPDATE);

            menus.put(LangConfig.get("atm.update"), MenuVal.UPDATE_ATM);
            menus.put(LangConfig.get("atm.cassette.block"), MenuVal.BLOCK_ATM_CASSETTE);
            menus.put(LangConfig.get("atm.cassette.unblock"), MenuVal.UNBLOCK_ATM_CASSETTE);
        }

        if (!Role.ANONYMOUS.equals(role)) {
            menus.put(LangConfig.get("card.order"), MenuVal.CLIENT_ORDER_CARD);
            menus.put(LangConfig.get("logout"), MenuVal.LOGOUT);
            menus.put(LangConfig.get("profile"), MenuVal.PROFILE);
            menus.put(LangConfig.get("language.change"), MenuVal.LANGUAGE_CHANGE);
        }

        if (Role.ANONYMOUS.equals(role)&&!(Objects.nonNull(CardRep.sessionCard)) ){
            menus.put(LangConfig.get("login"), MenuVal.LOGIN);
            menus.put(LangConfig.get("atm.service"), MenuVal.ATM_SERVICES);
        }

        if (Objects.nonNull(CardRep.sessionCard)) {
            menus.put(LangConfig.get("balance"), MenuVal.BALANCE);
            menus.put(LangConfig.get("take.cash"), MenuVal.TAKE_CASH);
            menus.put(LangConfig.get("operation.service"), MenuVal.OPERATION_SERVICE);
            menus.put(LangConfig.get("history"), MenuVal.HISTORY);
            menus.put(LangConfig.get("conversation"), MenuVal.CONVERTATION);
            menus.put(LangConfig.get("card2card"), MenuVal.CARD_2_CARD);
            menus.put("Back", MenuVal.CONVERTATION);
        }

        menus.put(LangConfig.get("quit"), MenuVal.QUIT);
        return menus;
    }

    public static void show() {
        int index = 1;

        for (String key : menus().keySet()) {
            if (key!=null)
            switch (key) {
                case "Order card", "заказ карт", "Karta buyurtma berish" -> Print.println(Color.CYAN, "C. " + key);
                case "logout", "Profildan chiqish", "покидать" -> Print.println(Color.CYAN, "L. " + key);
                case "профиль", "Profil", "Profile" -> Print.println(Color.CYAN, "P. " + key);
                case "Dasturdan chiqish", "выйти", "quit" -> Print.println(Color.CYAN, "Q. " + key);
                case "Tilni o`zgartirish", "Изменение языка", "Language change" -> Print.println(Color.CYAN, "H. " + key);
                default -> Print.println((index++) + ". " + key);
            }
        }

    }

    public static void servicesMenu() {
        Print.println("1.Change password");
        Print.println("2.SMS service");
    }

}



