package uz.jl.models.atm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.jl.configs.LangConfig;
import uz.jl.enums.CassetteType;
import uz.jl.enums.Money;
import uz.jl.enums.Status;
import uz.jl.models.Auditable;

import javax.xml.validation.ValidatorHandler;
import java.util.Date;

/**
 * @author Elmurodov Javohir, Mon 6:14 PM. 11/29/2021
 */
@Getter
@Setter
@NoArgsConstructor
public class Cassette extends Auditable {
    private static Cassette cassette;
    private Money currencyValue;
    private CassetteType cassetteType;
    private String currencyCount;
    private Status status;
    private String atmId;
    private double balance;

    public Cassette(CassetteType cassetteType, Money currencyValue, String currencyCount) {
        this.currencyValue = currencyValue;
        this.cassetteType = cassetteType;
        this.currencyCount = currencyCount;
    }

    private void calculatorBalance(){
        this.balance = Double.parseDouble(this.getCurrencyCount())*this.getCurrencyValue().getValue();
    }

    @Override
    public String toString() {
        return "\n"+LangConfig.get("Cassette.components") +
                "\n"+LangConfig.get("Currency.Value") + currencyValue +
                "\n"+LangConfig.get("Type") + cassetteType +
                "\n"+LangConfig.get("Currency.Count") + currencyCount + '\'' +
                "\n"+ LangConfig.get("Status") + status +
                "\n";
    }
}
