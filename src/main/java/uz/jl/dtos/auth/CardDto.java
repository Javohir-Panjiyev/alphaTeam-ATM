package uz.jl.dtos.auth;

import lombok.*;
import uz.jl.enums.card.CardType;

/**
 * @author Akhmedov Dilshodbek, вс 19:53. 12.12.2021
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {
    private String pan;
    private String expiry;
    private String password;
    private CardType cardType;
    private double balance;
}
