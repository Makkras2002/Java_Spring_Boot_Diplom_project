package com.makkras.shop.service.impl;

import com.makkras.shop.entity.RoleType;
import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.repo.UserJpaRepository;
import com.makkras.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserService implements UserService {
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public CustomUserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean checkIfUserIsValidForRegistration(String login, String email) {
        return !userJpaRepository.existsByLoginOrEmail(login, email);
    }

    @Override
    public boolean checkIfUserWithSuchLoginIsAlreadyPresent(String login) {
        return userJpaRepository.existsByLogin(login);
    }

    @Override
    public boolean checkIfUserWithSuchEmailIsAlreadyPresent(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public void registerUser(User user) {
        userJpaRepository.save(user);
    }

    @Override
    public void updateUserLogin(String newLogin, String oldLogin) throws CustomServiceException {
        if (userJpaRepository.updateUserLogin(newLogin, oldLogin) != 1) {
            throw new CustomServiceException("Error occurred during login update!");
        }
    }

    @Override
    public void updateUserEmail(String newEmail, String oldEmail, String login) throws CustomServiceException {
        if (userJpaRepository.updateUserEmail(newEmail, oldEmail, login) != 1) {
            throw new CustomServiceException("Error occurred during email update!");
        }
    }

    @Override
    public void updateUserPassword(String newPassword, String login) throws CustomServiceException {
        if (userJpaRepository.updateUserPassword(newPassword, login) != 1) {
            throw new CustomServiceException("Error occurred during password update!");
        }
    }

    @Override
    public Optional<User> findActiveUserByLogin(String login) {
        return userJpaRepository.findByLoginAndIsActive(login, true);
    }

    @Override
    public List<User> getAllUsers() {
        return userJpaRepository.findAll();
    }

    @Override
    public boolean updateUserActivityStatus(Long userId) {
        Optional<User> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isPresent()) {
            userJpaRepository.updateUserActivityStatus(!optionalUser.get().isActive(), userId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean updateUserAuthority(Long userId) {
        Optional<User> optionalUser = userJpaRepository.findById(userId);
        if (optionalUser.isPresent()) {
            if (optionalUser.get().getRole().getRoleType().equals(RoleType.CLIENT)) {
                userJpaRepository.updateUserAuthority(RoleType.EMPLOYEE.getRoleId(), userId);
                return true;
            } else if (optionalUser.get().getRole().getRoleType().equals(RoleType.EMPLOYEE)) {
                userJpaRepository.updateUserAuthority(RoleType.CLIENT.getRoleId(), userId);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<User> getAllUsersAndOrderByLogin() {
        return userJpaRepository.findAllByOrderByLogin();
    }

    @Override
    public List<User> getAllUsersAndOrderByEmail() {
        return userJpaRepository.findAllByOrderByEmail();
    }

    @Override
    public List<User> getAllUsersAndOrderByRoleDesc() {
        return userJpaRepository.findAllByOrderByRoleDesc();
    }

    @Override
    public List<User> getAllUsersAndOrderByRoleAsc() {
        return userJpaRepository.findAllByOrderByRoleAsc();
    }

    @Override
    public List<User> getAllUsersAndOrderByActivityDesc() {
        return userJpaRepository.findAllByOrderByIsActiveDesc();
    }

    @Override
    public List<User> getAllUsersAndOrderByActivityAsc() {
        return userJpaRepository.findAllByOrderByIsActiveAsc();
    }

    @Override
    public List<User> getFilteredUsers(String login,String email, String role) {
        return userJpaRepository.findAllByLoginLikeAndEmailLikeAndRoleLike(login,email,role);
    }
}