package com.evox.evoxbackend.repository;

import com.evox.evoxbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepositoryy extends JpaRepository<User, Integer> {
    @Query(value = "WITH RECURSIVE user_tree AS (\n" +
            "  SELECT *\n" +
            "  FROM users\n" +
            "  WHERE id = :userId\n" +
            "  UNION ALL\n" +
            "  SELECT u.*\n" +
            "  FROM users u\n" +
            "    INNER JOIN user_tree ut ON u.parent_id = ut.id\n" +
            ")\n" +
            "SELECT * FROM user_tree", nativeQuery = true)
    List<User> findUserAndDescendants(@Param("userId") Integer userId);




}
