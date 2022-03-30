package uz.jl.services.atm;

import uz.jl.models.atm.Atm;

import java.util.List;

public class CheckUniqueLocation {
    public static boolean checkUnique(List<Atm> list, String location){
        for (Atm atm:list) {
            if (atm.getLocation().equals(location)){
                return false;
            }
        }
        return true;
    }
}
