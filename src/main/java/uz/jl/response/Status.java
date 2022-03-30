package uz.jl.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Elmurodov Javohir, Mon 6:30 PM. 12/6/2021
 */

@Getter
@AllArgsConstructor
public enum Status {
    HTTP_OK(200),
    HTTP_NOT_FOUND(404),
    HTTP_BAD_REQUEST(405),
    HTTP_FORBIDDEN(403),
    UNDEFINED(-1);
    private final Integer code;


    public static Status getByCode(Integer code) {
        for (Status status : values()) {
            if (status.getCode().equals(code)) return status;
        }
        return UNDEFINED;
    }
}

