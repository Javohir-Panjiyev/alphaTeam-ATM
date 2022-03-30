package uz.jl.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import uz.jl.dtos.BaseGenericDto;
import uz.jl.models.auth.AuthUser;
import uz.jl.enums.Status;

import java.util.List;

/**
 * @author Elmurodov Javohir, Mon 6:24 PM. 12/6/2021
 */


@Getter
@Setter
@AllArgsConstructor
public class AuthUserDto extends BaseGenericDto {
    private String username;
    private String phoneNumber;
    private Status status;

    //    private List<Cards> cards;
    // add some other fields
}
