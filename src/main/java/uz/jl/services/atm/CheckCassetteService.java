package uz.jl.services.atm;

import uz.jl.enums.Money;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;

/**
 * @author Juraev Nodirbek, пт 15:33. 10.12.2021
 */
public class CheckCassetteService {

    public static boolean checkCurrencyCount(String count) {
        Integer sum=Integer.parseInt(count);
        for (int i = 0; i < count.length(); i++) {
            if (!(count.charAt(i) >= 48 && count.charAt(i) <= 57) && sum>5000) {
                return false;
            }
        }
        return true;
    }
}
