package uz.jl;

import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.enums.auth.Role;
import uz.jl.repository.card.CardRep;
import uz.jl.ui.AtmServiceUI;
import uz.jl.ui.BranchUI;
import uz.jl.ui.CardUI;
import uz.jl.ui.MenuUI;
import uz.jl.ui.adminUI.AdminUI;
import uz.jl.ui.atmUI.AtmUI;
import uz.jl.ui.authUI.AuthUI;
import uz.jl.ui.clientUI.ClientUI;
import uz.jl.ui.employeeUI.EmployeeUI;
import uz.jl.ui.hrUI.HrUI;
import uz.jl.utils.Color;
import uz.jl.utils.Input;
import uz.jl.utils.Print;

import java.util.Objects;

/**
 * @author Elmurodov Javohir, Mon 6->14 PM. 11/29/2021
 */
public class App {
    static AdminUI adminUI;
    static AtmUI atmUI;
    static AuthUI authUI;
    static BranchUI branchUI;
    static ClientUI clientUI;
    static EmployeeUI employeeUI;
    static HrUI hrUI;
    static CardUI cardUI;
    static Session session;
    static AtmServiceUI atmServiceUI;

    static {
        adminUI = AdminUI.getInstance();
        atmUI = AtmUI.getInstance();
        authUI = AuthUI.getInstance();
        branchUI = BranchUI.getInstance();
        clientUI = ClientUI.getInstance();
        employeeUI = EmployeeUI.getInstance();
        cardUI = CardUI.getInstance();
        hrUI = HrUI.getInstance();
        session = Session.getInstance();
        atmServiceUI = AtmServiceUI.getInstance();
    }


    public static void main(String[] args) {
        run(args);
    }

    private static void run(String[] args) {
        String choice = null;
        MenuUI.show();
        choice = Input.getStr("?->");
        Role role = session.getUser().getRole();
        if (role.equals(Role.SUPER_ADMIN)) {
            switch (choice.toUpperCase()) {
//                case "1" -> branchUI.create();
//                case "2" -> branchUI.block();
//                case "3" -> branchUI.unblock();
//                case "4" -> branchUI.delete();
//                case "5" -> branchUI.list();

                case "1" -> adminUI.create();
                case "2" -> adminUI.block();
                case "3" -> adminUI.unblock();
                case "4" -> adminUI.delete();
                case "5" -> adminUI.list();
            }
        } else if (role.equals(Role.ADMIN)) {
            switch (choice.toUpperCase()) {
                case "1" -> hrUI.create();
                case "2" -> hrUI.block();
                case "3" -> hrUI.unblock();
                case "4" -> hrUI.delete();
                case "5" -> hrUI.list();

                case "6" -> atmUI.create();
                case "7" -> atmUI.block();
                case "8" -> atmUI.unblock();
                case "9" -> atmUI.delete();
                case "10" -> atmUI.list();
            }
        } else if (role.equals(Role.HR)) {
            switch (choice.toUpperCase()) {
                case "1" -> employeeUI.create();
                case "2" -> employeeUI.block();
                case "3" -> employeeUI.unblock();
                case "4" -> employeeUI.delete();
                case "5" -> employeeUI.list();
            }
        } else if (role.equals(Role.EMPLOYEE)) {
            switch (choice.toUpperCase()) {
                case "1" -> clientUI.create();
                case "2" -> clientUI.block();
                case "3" -> clientUI.unblock();
                case "4" -> clientUI.delete();
                case "5" -> clientUI.list();
                case "6" -> cardUI.create();
                case "7" -> cardUI.block();
                case "8" -> cardUI.unblock();
                case "9" -> cardUI.delete();
                case "10" -> cardUI.uptade();



                case "11" -> atmUI.update();
                case "12" -> atmUI.blockCassette();
                case "13" -> atmUI.unblockCassette();
            }
        } else if (role.equals(Role.ANONYMOUS) && Objects.isNull(CardRep.sessionCard)) {
            switch (choice.toUpperCase()) {
                case "1" -> authUI.login();
                case "2" -> atmServiceUI.services();
                default -> Print.println(Color.RED, LangConfig.get("Wrong.Choice"));
            }
        }

        else if (role.equals(Role.ANONYMOUS)){

            switch (choice.toUpperCase()) {
                case "1" -> atmServiceUI.balance();
                case "2" -> atmServiceUI.takeCash();
                case "3" -> atmServiceUI.operations();
                case "4" -> atmServiceUI.history();
                case "5" -> clientUI.convertation();
                case "6" -> atmServiceUI.card2Card();
                case "7" -> {
                    CardRep.sessionCard= null;
                }
            }
        }

        if (!role.equals(Role.ANONYMOUS)) {
            if (choice.startsWith("c")) {
                clientUI.orderCard();
            } else if (choice.startsWith("l")) {
                authUI.logout();
            } else if (choice.startsWith("p")) {
                authUI.profile();
            } else if (choice.startsWith("h")) {
                authUI.change();
            }
        }

        if (choice.startsWith("q")) {
            authUI.quit();
            return;
        }
        main(args);
    }

}
