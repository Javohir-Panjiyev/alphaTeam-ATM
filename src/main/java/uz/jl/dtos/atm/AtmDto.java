package uz.jl.dtos.atm;

import uz.jl.enums.Status;
import uz.jl.models.atm.Cassette;

import java.util.List;

/**
 * @author Juraev Nodirbek, сб 19:28. 11.12.2021
 */
public class AtmDto {
    private String location;
    private String branchId;
    private Status status = Status.ACTIVE;
    private List<Cassette> cassette;

}
