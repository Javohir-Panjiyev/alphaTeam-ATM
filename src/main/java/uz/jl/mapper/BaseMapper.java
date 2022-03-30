package uz.jl.mapper;

import uz.jl.dtos.BaseDto;

/**
 * @param <E>  -> Entity
 * @param <D>  -> Dto
 */
public abstract class BaseMapper<E, D extends BaseDto> {
    abstract E fromDto(D d);

    abstract D toDto(E e);
}
