package uz.jl.services;

/**
 * @author Elmurodov Javohir, Mon 5:44 PM. 12/6/2021
 */

import lombok.Getter;
import lombok.Setter;
import uz.jl.response.ResponseEntity;

import java.util.List;

/**
 * @param <E> -> Entity (Db Table representation)
 * @param <R> -> Repository (Which executes SQL queries)
 * @param <M> -> Mapper (Which maps entity to Dto or vise versa)
 */

@Setter
@Getter
public abstract class BaseAbstractService<E, R, M> {
    protected R repository;
    protected M mapper;

    protected BaseAbstractService(R repository, M mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public abstract ResponseEntity<List<E>> getAll();

    public abstract ResponseEntity<E> get(String id);



}
