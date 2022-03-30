package uz.jl.dtos.auth;

import lombok.*;
import uz.jl.models.card.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akhmedov Dilshodbek, вс 13:30. 12.12.2021
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserProfileDto {
    private String username;
    private String password;
    private String phoneNumber;
    private List<CardDto> cards = new ArrayList<>();
}
