package uz.jl.ui.adminUI;

import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.AuthUsersServices;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;
import uz.jl.utils.Print;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static uz.jl.utils.Color.BLUE;
import static uz.jl.utils.Color.RED;
import static uz.jl.utils.Input.getStr;
import static uz.jl.utils.Print.println;


/**
 * @author Elmurodov Javohir, Wed 4:47 PM. 12/8/2021
 */
public class AdminUI extends BaseAbstractUI implements BaseUI {

    private static final AuthUsersServices authServices = AuthUsersServices.getInstance();
    private static AdminUI adminUI;

    public static AdminUI getInstance() {
        if (Objects.isNull(adminUI)) {
            return adminUI = new AdminUI();
        }
        return adminUI;
    }

    private AdminUI() {
    }

    @Override
    public void create() {
        String fullname = getStr(LangConfig.get("fulname"));
        String pasportSeria = getStr(LangConfig.get("passport.series"));
        String pasportNumber = getStr(LangConfig.get("passport.number"));
        String username = getStr(LangConfig.get("username"));
        String password = getStr(LangConfig.get("password"));
        String phoneNumber = getStr(LangConfig.get("Phone.number"));
        ResponseEntity<String> response = authServices.creat(fullname, pasportSeria, pasportNumber, username, password, phoneNumber, Role.ADMIN);
        show(response);
    }

    @Override
    public void block() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.block(username, Role.ADMIN);
        show(response);
    }

    @Override
    public void unblock() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.unBlock(username, Role.ADMIN);
        show(response);
    }

    @Override
    public void delete() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.delete(username, Role.ADMIN);
        show(response);
    }

    @Override
    public boolean list() {
        List<AuthUserDto> usersDto = authServices.list(Role.ADMIN);
        if (usersDto.isEmpty()) {
            show(new ResponseEntity<>(LangConfig.get("Empty.list"), uz.jl.response.Status.HTTP_NOT_FOUND));
            return false;
        }
        for (AuthUserDto userDto : usersDto) {
            String color = userDto.getStatus().equals(Status.ACTIVE) ? BLUE : RED;
            String emoje = userDto.getStatus().equals(Status.ACTIVE) ? "🎯" : "🔒";
            println(color, userDto.getUsername() + " \t" + userDto.getPhoneNumber() + " \t" + emoje);
        }
        show(new ResponseEntity<>("  ------------------------", uz.jl.response.Status.HTTP_OK));
        return true;

    }

}
