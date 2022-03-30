package uz.jl.models.personal;

import lombok.*;
import uz.jl.models.Auditable;

/**
 * @author Elmurodov Javohir, Mon 5:22 PM. 12/6/2021
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"serial", "number"}, callSuper = false)
public class Passport extends Auditable {
    private String serial;
    private String number;
    private String ownerId;
    private String fullName;
}
