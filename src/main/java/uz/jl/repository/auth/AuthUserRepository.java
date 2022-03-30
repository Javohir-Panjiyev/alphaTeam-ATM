package uz.jl.repository.auth;

import com.google.gson.reflect.TypeToken;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.personal.Passport;
import uz.jl.repository.BaseRepository;
import uz.jl.services.filesystems.DB;
import uz.jl.services.filesystems.FileData;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author Elmurodov Javohir, Mon 6:19 PM. 11/29/2021
 */
public class AuthUserRepository extends BaseRepository<AuthUser> {
    private static final FileData<Passport> fileData = new FileData<>();
    private static final Type type = new TypeToken<List<Passport>>() {}.getType();

    private static AuthUserRepository instance;

    public static AuthUserRepository getInstance() {
        if (instance == null) {
            instance = new AuthUserRepository();
        }
        return instance;
    }

    @Override
    protected void save(AuthUser user) {

    }

    public Passport findByPosport(String passportSeria, String passportNumber, String file) {
        for (Passport passport : fileData.myReader(file, type)) {
            if (passport.getSerial().equals(passportSeria) && passport.getNumber().equals(passportNumber)) {
                return passport;
            }
        }
        return null;
    }

    public AuthUser findByUserName(String username) {
        for (AuthUser user : DB.getUsers()) {
            if (user.getUsername().equals(username)) return user;
        }
        return null;
    }

    public boolean checkingData(String passportSeria, String passportNumber, String phoneNumber) {
        if (passportSeria.length() == 2 && passportNumber.length() == 7 && phoneNumber.length() == 9) {
            for (int i = 0; i < 2; i++) {
                if (passportSeria.charAt(i) - 'A' < 0 || passportSeria.charAt(i) - 'A' > 26) {
                    return false;
                }
            }
            for (int i = 0; i < 7; i++) {
                if (passportNumber.charAt(i) - '0' < 0 || passportNumber.charAt(i) - '0' > 9) {
                    return false;
                }
            }
            for (int i = 0; i < 9; i++) {
                if (phoneNumber.charAt(i) - '0' < 0 || phoneNumber.charAt(i) - '0' > 9) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

}
