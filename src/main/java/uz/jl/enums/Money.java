package uz.jl.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Juraev Nodirbek, пт 17:37. 10.12.2021
 */
@Getter
@AllArgsConstructor
public enum Money {
    MING_SUM(1000, CassetteType.UZS),
    IKKI_MING_SUM(2000, CassetteType.UZS),
    BESH_MING_SUM(5000, CassetteType.UZS),
    ON_MING_SUM(10000, CassetteType.UZS),
    YIGIRMA_MING_SUM(20000, CassetteType.UZS),
    ELLIK_MING_SUM(50000, CassetteType.UZS),
    YUZ_MING_SUM(100000, CassetteType.UZS),

    YIGIRMA_BESH_DOLLAR(25, CassetteType.USD),
    ELLIK_DOLLAR(50, CassetteType.USD),
    YUZ_DOLLAR(100, CassetteType.USD);

    private final double value;
    private final CassetteType type;

    public static Money findByValue(String value, CassetteType type) {
        if (type.equals(CassetteType.UZS)) {
            if (value.equals("1000")) {
                return MING_SUM;
            } else if (value.equals("2000")) {
                return IKKI_MING_SUM;
            } else if (value.equals("5000")) {
                return BESH_MING_SUM;
            } else if (value.equals("10000")) {
                return ON_MING_SUM;
            } else if (value.equals("20000")) {
                return YIGIRMA_MING_SUM;
            } else if (value.equals("50000")) {
                return ELLIK_MING_SUM;
            } else if (value.equals("100000")) {
                return YUZ_MING_SUM;
            }
        } else if (type.equals(CassetteType.USD)) {
            if (value.equals("25")) {
                return YIGIRMA_BESH_DOLLAR;
            } else if (value.equals("50")) {
                return ELLIK_DOLLAR;
            } else if (value.equals("100")) {
                return YUZ_DOLLAR;
            }
        }
        return null;
    }
}
