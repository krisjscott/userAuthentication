package com.example.userauth.repository;

import com.example.userauth.model.OtpToken;
import com.example.userauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    OtpToken findByUserEmail(String Email);
    void deleteByUser(User user);
}
