package com.makkras.shop.repo;

import com.makkras.shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User,Long> {
    Optional<User> findByLoginOrEmailAndIsActive(String login,String email,Boolean isActive);
    Optional<User> findByEmailAndIsActive(String email,Boolean isActive);
    Optional<User> findByLoginAndIsActive(String login,Boolean isActive);
    boolean existsByLoginOrEmail(String login, String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<User> findAllByOrderByLogin();
    List<User> findAllByOrderByEmail();
    List<User> findAllByOrderByRoleDesc();
    List<User> findAllByOrderByRoleAsc();
    List<User> findAllByOrderByIsActiveDesc();
    List<User> findAllByOrderByIsActiveAsc();

    @Query(value = """
    SELECT DISTINCT * FROM users JOIN user_roles ON users.role_id = user_roles.role_id 
    WHERE login LIKE ? AND 
    email LIKE ? AND 
    user_roles.role_name LIKE ?""",nativeQuery = true)
    List<User> findAllByLoginLikeAndEmailLikeAndRoleLike(String login, String email, String role);


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

    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET password=? WHERE email = ?",nativeQuery = true)
    int updateUserPasswordByUserEmail(String newPassword, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET is_active=? WHERE user_id = ?",nativeQuery = true)
    void updateUserActivityStatus(boolean activityStatus, Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Users SET role_id=? WHERE user_id = ?",nativeQuery = true)
    void updateUserAuthority(Long roleId, Long id);
}
