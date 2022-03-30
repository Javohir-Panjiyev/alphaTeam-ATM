package uz.jl.enums.card;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.naming.ldap.PagedResultsControl;

/**
 * @author Elmurodov Javohir, Mon 5:37 PM. 12/6/2021
 */


@Getter
@AllArgsConstructor
public enum CardType {
    UZCARD("8600"),
    HUMO("9860"),
    COBAGE("6320"),
    VISA("4790"),
    UNION_PAY("4567");
    private final String code;
}
