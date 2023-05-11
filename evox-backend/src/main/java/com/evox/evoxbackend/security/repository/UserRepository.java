package com.evox.evoxbackend.security.repository;
import com.evox.evoxbackend.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer>  , QueryByExampleExecutor<User> {

    User findByUsernameIgnoreCase(String username);

}
