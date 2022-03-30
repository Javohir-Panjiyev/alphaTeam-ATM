package uz.jl.services.auth;

import com.google.gson.reflect.TypeToken;
import uz.jl.configs.LangConfig;
import uz.jl.configs.Session;
import uz.jl.dtos.auth.AuthUserDto;
import uz.jl.dtos.auth.AuthUserProfileDto;
import uz.jl.dtos.auth.CardDto;
import uz.jl.enums.auth.Role;
import uz.jl.enums.card.CardType;
import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;
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
import java.util.Objects;

/**
 * @author Elmurodov Javohir, Mon 6:23 PM. 11/29/2021
 */

public class AuthUserService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {
    private static AuthUserService authUserService;

    private AuthUserService(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    public static AuthUserService getInstance() {
        if (authUserService == null) {
            authUserService = new AuthUserService(AuthUserRepository.getInstance(), AuthUserMapper.getInstance());
        }
        return authUserService;
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

    public ResponseEntity<String> login(String username, String password) {
        AuthUser user = repository.findByUserName(username);
        if (user.getStatus().equals(uz.jl.enums.Status.BLOCKED)){
            return new ResponseEntity<>(LangConfig.get("User.blocked"), Status.HTTP_FORBIDDEN);
        }
        if (Objects.isNull(user) || !user.getPassword().equals(password) ) {
            return new ResponseEntity<>(LangConfig.get("Bad.Credentials"), Status.HTTP_FORBIDDEN);
        }
        Session.getInstance().setUser(user);
        return new ResponseEntity<>(LangConfig.get("Successfully"));
    }

    public ResponseEntity<String> logout() {
        Session.getInstance().setUser(new AuthUser(Role.ANONYMOUS));
        return new ResponseEntity<>(LangConfig.get("Come.again"));
    }

    public AuthUserProfileDto profile(String id) {
        List<AuthUser> users = DB.getUsers();
        AuthUserProfileDto authUserProfileDto = new AuthUserProfileDto();
        for (AuthUser user : users) {
            if (user.getId().equals(id)) {
                authUserProfileDto.setUsername(user.getUsername());
                authUserProfileDto.setPassword("*".repeat(user.getPassword().length()));
                authUserProfileDto.setPhoneNumber(user.getPhoneNumber());
                break;
            }
        }
        cardDtoCreate(id);
        return authUserProfileDto;
    }

    private void cardDtoCreate(String id) {
        FileData<Card> fileData = new FileData<>();
        String file = "src/main/resources/db/card.json";
        Type type = new TypeToken<List<Card>>() {
        }.getType();

        List<Card> cards = fileData.myReader(file, type);
        List<CardDto> cardDtos = new ArrayList<>();

        for (Card card : cards) {
            if (card.getOwnerId().equals(id)){
                CardDto cardDto = new CardDto();
                cardDto.setPan(card.getPan());
                cardDto.setExpiry(card.getExpiry());
                String password = card.getPassword().substring(0, 5) + "******" + card.getPassword().substring(12);
                cardDto.setPassword(password);
                cardDto.setCardType(card.getCardType());
                cardDto.setBalance(card.getBalance());
                cardDtos.add(cardDto);
            }
        }
    }

}
