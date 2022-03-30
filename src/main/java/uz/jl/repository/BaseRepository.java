package uz.jl.repository;

/**
 * @author Elmurodov Javohir, Mon 5:58 PM. 12/6/2021
 */

import uz.jl.models.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <E> -> Entity .....
 */
public abstract class BaseRepository<E extends BaseEntity> {

    protected void save(E e) {
        // TODO: 12/6/2021 write some logic
    }

    protected void delete(String id) {
        // TODO: 12/6/2021 write some logic
    }

    protected void delete(E e) {
        // TODO: 12/6/2021 write some logic
    }

    protected E get() {
        // TODO: 12/6/2021 write some logic
        return null;
    }

    protected List<E> getAll() {
        // TODO: 12/6/2021 write some logic
        return new ArrayList<>();
    }

//    protected List<E> getAll(Criteria criteria) {
//        // TODO: 12/6/2021 write some logic
//        return new ArrayList<>();
//    }


}
