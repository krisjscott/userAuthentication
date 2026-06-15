package com.example.userauth.repository;

import com.example.userauth.model.OtpToken;
import com.example.userauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    OtpToken findFirstByUserEmailOrderByCreateTimeDesc(String email);

    @Modifying
    @Query("DELETE FROM OtpToken t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);
}
