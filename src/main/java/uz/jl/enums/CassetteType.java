package uz.jl.enums;

public enum CassetteType {
    UZS, USD;

    public static CassetteType findByName(String type) {
        if (type.equalsIgnoreCase("usd")) return USD;
        else if (type.equalsIgnoreCase("uzs")) return UZS;
        return null;
    }
}
