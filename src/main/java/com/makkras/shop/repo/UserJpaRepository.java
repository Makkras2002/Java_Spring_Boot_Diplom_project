package com.makkras.shop.repo;

import com.makkras.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByLoginOrEmailAndIsActive(String login,String email,Boolean isActive);
    Optional<User> findByLoginAndIsActive(String login,Boolean isActive);
    boolean existsByLoginOrEmail(String login, String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET login=? WHERE login =?",nativeQuery = true)
    int updateUserLogin(String newLogin, String oldLogin);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET email=? WHERE email =? AND login = ?",nativeQuery = true)
    int updateUserEmail(String newEmail, String oldEmail, String login);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET password=? WHERE login = ?",nativeQuery = true)
    int updateUserPassword(String newPassword, String login);
}
