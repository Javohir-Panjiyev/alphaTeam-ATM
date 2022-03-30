package uz.jl.models.history;

import lombok.*;
import uz.jl.configs.LangConfig;
import uz.jl.enums.card.CardType;
import uz.jl.utils.BaseUtils;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class History {
    private String id = BaseUtils.genID();
    private String senderPan;
    private String senderName;
    private String receiverPan;
    private String receiverName;
    private CardType cardType;
    private String description;
    private double summa;
    private double balance;
    private final Date date = new Date();

    @Override
    public String toString() {
        return
                LangConfig.get("senderPan") + senderPan + '\n' +
                        LangConfig.get("senderName") + senderName + '\n' +
                        LangConfig.get("receiverPan") + receiverPan + '\n' +
                        LangConfig.get("receiverName") + receiverName + '\n' +
                        LangConfig.get("cardType") + cardType + '\n' +
                        LangConfig.get("description") + description + '\n' +
                        "summa= " + summa + '\n' +
                        LangConfig.get("balance") + balance + '\n' +
                        LangConfig.get("date") + date + '\n' +
                        "------------------------------------------------------------";
    }
}
