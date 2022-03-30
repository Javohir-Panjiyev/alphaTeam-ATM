package uz.jl.dtos.auth;

import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseDto;

/**
 * @author Elmurodov Javohir, Mon 6:23 PM. 12/6/2021
 */

@Getter
@Setter
public class AuthUserCreateDto implements BaseDto {
    private String username;
    private String password;
    private String phoneNumber;

}
