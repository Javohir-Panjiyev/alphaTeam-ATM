package uz.jl.models.atm;

import lombok.*;
import uz.jl.configs.LangConfig;
import uz.jl.enums.Status;
import uz.jl.models.Auditable;

import java.util.List;
import java.util.Objects;

/**
 * @author Juraev Nodirbek, чт 20:10. 09.12.2021
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Atm extends Auditable {
    private String location;
    private String branchId;
    private Status status = Status.ACTIVE;
    private List<Cassette> cassette;
    private double balance;

    @Override
    public String toString() {
        return LangConfig.get("Location") + location + '\'' +
                "\n" + LangConfig.get("Status") + status +
                "\n" + LangConfig.get("Cassettes") + "\n" + cassette + "\n";
    }

    private void calculateBalance() {
        if (Objects.nonNull(this.getCassette())) {
            double balance = 0;
            for (Cassette cassette1 : this.getCassette()) {
                balance += cassette1.getBalance();
            }
            this.balance = balance;
        }
    }

}
