package uz.jl.models;

import lombok.*;
import uz.jl.configs.Session;
import uz.jl.utils.BaseUtils;

import java.util.Date;

/**
 * @author Elmurodov Javohir, Mon 4:45 PM. 12/6/2021
 */


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class Auditable implements BaseEntity {
    private String id = BaseUtils.genID();
    private String createdBy;
    private Date createdAt = new Date();
    private String updatedBy;
    private Date updatedAt;
    private int deleted;
}
