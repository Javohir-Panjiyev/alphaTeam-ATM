package uz.jl.mapper.auth;

import uz.jl.mapper.BaseMapper;
import uz.jl.models.auth.AuthUser;

/**
 * @author Elmurodov Javohir, Mon 6:20 PM. 12/6/2021
 */
public class AuthUserMapper {

    public static AuthUserMapper instance;

    public static AuthUserMapper getInstance() {
        if (instance == null) {
            instance = new AuthUserMapper();
        }
        return instance;
    }
}
