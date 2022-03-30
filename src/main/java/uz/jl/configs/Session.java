package uz.jl.configs;

import lombok.Getter;
import lombok.Setter;
import uz.jl.models.auth.AuthUser;
import uz.jl.models.card.Card;

/**
 * @author Elmurodov Javohir, Wed 4:11 PM. 12/8/2021
 */

public class Session {
    private static Session session;

    @Getter
    @Setter
    private AuthUser user;


    public static Session getInstance() {
        if (session == null) {
            session = new Session();
            session.setUser(new AuthUser());
        }
        return session;
    }
}
