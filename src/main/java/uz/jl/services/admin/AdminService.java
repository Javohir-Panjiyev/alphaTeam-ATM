package uz.jl.services.admin;


import uz.jl.mapper.auth.AuthUserMapper;
import uz.jl.models.auth.AuthUser;
import uz.jl.repository.auth.AuthUserRepository;
import uz.jl.response.ResponseEntity;
import uz.jl.response.Status;
import uz.jl.services.BaseAbstractService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Akhmedov Dilshodbek, чт 22:11. 09.12.2021
 */


public class AdminService extends BaseAbstractService<AuthUser, AuthUserRepository, AuthUserMapper> {

    protected AdminService(AuthUserRepository repository, AuthUserMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public ResponseEntity<List<AuthUser>> getAll() {
        List<AuthUser> users = new ArrayList<>();
        return new ResponseEntity<>(users, Status.HTTP_NOT_FOUND);
    }

    @Override
    public ResponseEntity<AuthUser> get(String id) {
        return new ResponseEntity<>(new AuthUser());
    }
}