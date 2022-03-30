package uz.jl.response;

/**
 * @author Elmurodov Javohir, Mon 6:29 PM. 12/6/2021
 */

import lombok.Getter;
import lombok.Setter;
import uz.jl.configs.LangConfig;

/**
 * @param <RD> -> Response Data
 */

@Getter
@Setter
public class ResponseEntity<RD> {
    private RD data;
    private Integer status;

    public ResponseEntity() {
        this.status = Status.HTTP_OK.getCode();
    }

    public ResponseEntity(RD data, Integer code) {
        validateCode(code);
        this.data = data;
        this.status = code;
    }

    public ResponseEntity(RD data, Status status) {
        this.data = data;
        this.status = status.getCode();
    }

    public ResponseEntity(RD data) {
        this.data = data;
        this.status = Status.HTTP_OK.getCode();
    }

    private void validateCode(Integer code) {
        Status status = Status.getByCode(code);
        if (status.equals(Status.UNDEFINED)) {
            throw new RuntimeException(LangConfig.get("Code.is.invalid"));
        }
    }

}
