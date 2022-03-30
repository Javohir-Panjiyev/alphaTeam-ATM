package uz.jl.models.card;

import lombok.*;
import uz.jl.enums.Status;
import uz.jl.enums.card.CardType;
import uz.jl.models.Auditable;

import java.util.ArrayList;

/**
 * @author Elmurodov Javohir, Mon 5:34 PM. 12/6/2021
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"pan"}, callSuper = false)
public class Card extends Auditable {
    private String pan;
    private String expiry;
    private String password;
    private CardType cardType;
    private Status cardStatus;
    private String ownerId;
    private double balance;

    @Override
    public String toString() {
        return "Card{" +
                "pan='" + pan + '\'' +
                ", expiry='" + expiry + '\'' +
                ", cardType=" + cardType +
                ", balance=" + balance +
                '}';
    }
}
