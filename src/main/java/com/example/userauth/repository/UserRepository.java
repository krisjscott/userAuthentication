package com.example.userauth.repository;

import com.example.userauth.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByMail(String email);

    Optional<User> findByVerification(String verification);
}
