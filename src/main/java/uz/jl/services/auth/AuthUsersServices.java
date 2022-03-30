package uz.jl.services.auth;

import com.google.gson.reflect.TypeToken;
import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.enums.auth.Role;
import uz.jl.enums.settings.Language;
import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.personal.Passport;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.BaseAbstractService;
import uz.jl.services.filesystems.DB;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Akhmedov Dilshodbek, пт 12:30. 10.12.2021
 */
public class AuthUsersServices extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {

    private static final Passport passport = new Passport();
    private static final FileData<Passport> fileData = new FileData<>();
    private static final Type type = new TypeToken<List<Passport>>() {
    }.getType();

    public static AuthUsersServices authService;

    private AuthUsersServices(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    public static AuthUsersServices getInstance() {
        if (authService == null) {
            authService = new AuthUsersServices(AuthUserRepository.getInstance(), AuthUserMapper.getInstance());
        }
        return authService;
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


    public ResponseEntity<String> creat(String fullname, String passportSeria, String passportNumber, String username, String password, String phoneNumber, Role role) {

        if (!repository.checkingData(passportSeria.toUpperCase(Locale.ROOT), passportNumber, phoneNumber))
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);

        if (Objects.nonNull(repository.findByUserName(username))
                && Objects.nonNull(repository.findByPosport(passportSeria, passportNumber, "src/main/resources/db/pasports.json"))) {
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
        }

        AuthUser user = getAuthUserCreate(username, password, phoneNumber, role);

        createAuthUserPassport(fullname, passportSeria.toUpperCase(Locale.ROOT), passportNumber, user);

        return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
    }

    private void createAuthUserPassport(String fullname, String passportSeria, String passportNumber, AuthUser user) {
        passport.setSerial(passportSeria);
        passport.setNumber(passportNumber);
        passport.setFullName(fullname);
        passport.setOwnerId(user.getId());
        String file = "src/main/resources/db/pasports.json";
        List<Passport> passports = fileData.myReader(file, type);
        passports.add(passport);
        fileData.myWriter(passports, file);
    }

    private AuthUser getAuthUserCreate(String username, String password, String phoneNumber, Role role) {
        AuthUser user = new AuthUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhoneNumber("+998" + phoneNumber);
        user.setRole(role);
        user.setStatus(uz.jl.enums.Status.ACTIVE);
        user.setCreatedBy(Session.getInstance().getUser().getId());
        List<AuthUser> users = DB.getUsers();
        users.add(user);
        DB.writeUsers(users);
        return user;
    }

    public ResponseEntity<String> orderCard(String id) {
        String file = "src/main/resources/db/pasports.json";
        List<Passport> passports = fileData.myReader(file, type);
        for (Passport passport1 : passports) {
            if (passport1.getOwnerId().equals(id)) {
                if (Objects.nonNull(repository.findByPosport(passport1.getSerial(), passport1.getNumber(), "src/main/resources/db/requests.json")))
                    return new ResponseEntity<>(LangConfig.get("Sent.to.the.card"), Status.HTTP_FORBIDDEN);

                String reqFile = "src/main/resources/db/requests.json";
                List<Passport> reqPassports = fileData.myReader(reqFile, type);
                reqPassports.add(passport1);
                fileData.myWriter(passports, reqFile);
                return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public ResponseEntity<String> block(String username, Role role) {
        return getStringResponseEntity(username, role, uz.jl.enums.Status.ACTIVE, uz.jl.enums.Status.BLOCKED);
    }

    public ResponseEntity<String> unBlock(String username, Role role) {
        return getStringResponseEntity(username, role, uz.jl.enums.Status.BLOCKED, uz.jl.enums.Status.ACTIVE);
    }

    public ResponseEntity<String> delete(String username, Role role) {
        List<AuthUser> users = DB.getUsers();
        for (AuthUser user : users) {
            if (user.getUsername().equals(username) && user.getRole().equals(role)) {
                users.remove(user);
                DB.writeUsers(users);
                deletePassport(user.getId());
                return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    private void deletePassport(String id) {
        String file = "src/main/resources/db/pasports.json";
        List<Passport> passports = fileData.myReader(file, type);
        passports.removeIf(passport1 -> passport1.getOwnerId().equals(id));
        fileData.myWriter(passports, file);
    }

    private ResponseEntity<String> getStringResponseEntity(String username, Role role, uz.jl.enums.Status blocked, uz.jl.enums.Status active) {
        List<AuthUser> users = DB.getUsers();
        for (AuthUser user : users) {
            if (user.getUsername().equals(username) && user.getRole().equals(role)) {
                if (user.getStatus().equals(blocked)) {
                    user.setStatus(active);
                    DB.writeUsers(users);
                } else {
                    if (blocked.equals(uz.jl.enums.Status.BLOCKED)) {
                        return new ResponseEntity<>(LangConfig.get("User.unblocked"), Status.HTTP_FORBIDDEN);
                    }
                    else {
                        return new ResponseEntity<>(LangConfig.get("User.blocked"), Status.HTTP_FORBIDDEN);
                    }

                }
                return new ResponseEntity<>(LangConfig.get("Successfully"), Status.HTTP_OK);
            }
        }
        return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
    }

    public List<AuthUserDto> list(Role role) {
        List<AuthUser> users = DB.getUsers();
        List<AuthUserDto> usersDto = new ArrayList<>();
        for (AuthUser user : users) {
            if (Objects.nonNull(user.getCreatedBy()) && user.getRole().equals(role)) {
                AuthUserDto userDto = new AuthUserDto(user.getUsername(), user.getPhoneNumber(), user.getStatus());
                usersDto.add(userDto);
            }
        }
        return usersDto;
    }

    public static void changeLanguage(String lan) {
        if (lan.equals("uz")) {
            Session.getInstance().getUser().setLanguage(Language.getByCode("uz"));
        } else if (lan.equals("en")) {
            Session.getInstance().getUser().setLanguage(Language.getByCode("en"));
        } else if (lan.equals("ru")) {
            Session.getInstance().getUser().setLanguage(Language.getByCode("ru"));
        }
    }
}


