package uz.jl.models.auth;

import lombok.*;
import uz.jl.App;
import uz.jl.configs.AppConfig;
import uz.jl.enums.Status;
import uz.jl.enums.auth.Role;
import uz.jl.enums.settings.Language;
import uz.jl.models.Auditable;

/**
 * @author Elmurodov Javohir, Mon 6:18 PM. 11/29/2021
 */
@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "childBuilder")
public class AuthUser extends Auditable {
    private String username;
    private String password;
    private Status status;
    private Role role;
    private String phoneNumber;
    private Language language;


    public AuthUser(Role role) {
        this();
        this.role = role;
    }

    public AuthUser() {
        super();
        this.language = AppConfig.language;
        this.role = Role.ANONYMOUS;
    }
}
