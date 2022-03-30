package uz.jl.ui.employeeUI;

import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.response.ResponseEntity;
import uz.jl.services.auth.AuthUsersServices;
import uz.jl.services.employee.EmployeeService;
import uz.jl.ui.BaseAbstractUI;
import uz.jl.ui.BaseUI;

import java.util.List;
import java.util.Objects;

import static uz.jl.utils.Color.*;
import static uz.jl.utils.Input.getStr;
import static uz.jl.utils.Print.println;

/**
 * @author Elmurodov Javohir, Wed 4:49 PM. 12/8/2021
 */
public class EmployeeUI extends BaseAbstractUI implements BaseUI {
    private static final AuthUsersServices authServices = AuthUsersServices.getInstance();
    private static EmployeeUI employeeUI;

    public static EmployeeUI getInstance() {
        if (Objects.isNull(employeeUI)){
            return employeeUI=new EmployeeUI();
        }
        return employeeUI;
    }

    private EmployeeUI() {
    }

    public void create() {
        String fullname = getStr(LangConfig.get("fulname"));
        String pasportSeria = getStr(LangConfig.get("passport.series"));
        String pasportNumber = getStr(LangConfig.get("passport.number"));
        String username = getStr(LangConfig.get("username"));
        String password = getStr(LangConfig.get("password"));
        String phoneNumber = getStr(LangConfig.get("Phone.number"));
        ResponseEntity<String> response = authServices.creat(fullname, pasportSeria, pasportNumber, username, password, phoneNumber, Role.EMPLOYEE);
        show(response);
    }

    @Override
    public void block() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.block(username, Role.EMPLOYEE);
        show(response);
    }

    @Override
    public void unblock() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.unBlock(username, Role.EMPLOYEE);
        show(response);
    }

    @Override
    public void delete() {
        if (!list()) return;
        String username = getStr(LangConfig.get("username"));
        ResponseEntity<String> response = authServices.delete(username, Role.EMPLOYEE);
        show(response);
    }

    @Override
    public boolean list() {
        List<AuthUserDto> usersDto = authServices.list(Role.EMPLOYEE);
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

    public void giveCard() {
    //TODO give card qilib qoyilar
    }
}
