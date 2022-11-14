package com.makkras.shop.controller;

import com.google.gson.Gson;
import com.makkras.shop.entity.RoleType;
import com.makkras.shop.entity.User;
import com.makkras.shop.entity.UserRole;
import com.makkras.shop.exception.CustomServiceException;
import com.makkras.shop.security.SecurityUser;
import com.makkras.shop.service.MailService;
import com.makkras.shop.service.UserService;
import com.makkras.shop.service.impl.CustomMailService;
import com.makkras.shop.service.impl.CustomUserService;
import com.makkras.shop.validator.UserDataValidator;
import com.makkras.shop.validator.impl.CustomUserDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final  String USER_ALREADY_REGISTERED_ERROR = "Пользователь с таким логином или почтой уже зарегистрирован!";
    private static final String INVALID_DATA_ERROR = "Введены недопустимые данные!";
    private static final String PASSWORD_REPEATED_INCORRECTLY = "Введённые пароли не совпадают";
    private static final  String URL_TO_CONFIRM_REGISTRATION_BY_EMAIL = "/regconfirm";
    private static final String APP_HOST = "http://localhost:8080";
    private static final String USER_TO_CONFIRM_REG_ATTRIBUTE_NAME = "userAwaitingEmailConfirmationToRegister";
    private static final String ONLY_ACTIVE_USERS = "onlyActiveUsers";
    private static final String ONLY_INACTIVE_USERS = "onlyInactiveUsers";
    private static final String NONE_USERS_FOUND_SEARCH_ERROR = "Пользователи, соответствующие параметрам поиска, не были найдены!";
    private static Logger logger = LogManager.getLogger();
    private final UserService userService;
    private final UserDataValidator userDataValidator;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final Gson gson;

    @Autowired
    public UserController(CustomUserService userService, CustomUserDataValidator userDataValidator,
                          CustomMailService mailService) {
        this.userService = userService;
        this.userDataValidator = userDataValidator;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.mailService = mailService;
        gson = new Gson();
    }

    @GetMapping("/login")
    public String performLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "loginPage";
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        return "registrationPage";
    }

    @PostMapping("/register")
    public String performRegistration(Model model,
                                      @RequestParam String login,
                                      @RequestParam String password,
                                      @RequestParam String email, HttpServletRequest request) {
        Map<String,String> formValues = Map.of("login",login,"email",email,"password",password);
        if(userDataValidator.validateUserRegistrationData(formValues)) {
            if(userService.checkIfUserIsValidForRegistration(login, email)) {
                HttpSession currentSession = request.getSession();
                User user = new User(login,email,passwordEncoder.encode(password),
                        new UserRole(RoleType.CLIENT.getRoleId(),RoleType.CLIENT),true);
                currentSession.setAttribute(USER_TO_CONFIRM_REG_ATTRIBUTE_NAME, user);
                try {
                    mailService.sendVerificationEmail(user,APP_HOST+
                            request.getContextPath()+
                            URL_TO_CONFIRM_REGISTRATION_BY_EMAIL);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    logger.error(e.getMessage());
                }
                return "confirmRegistrationPage";
            } else {
                model.addAttribute("UserAlreadyExistsError",USER_ALREADY_REGISTERED_ERROR);
                model.addAttribute("formValues",formValues);
                return "registrationPage";
            }
        } else {
            model.addAttribute("formValues",formValues);
            return "registrationPage";
        }
    }

    @GetMapping("/regconfirm")
    public String confirmRegistration(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Optional<Object> userToRegisterOptional = Optional.ofNullable(session.getAttribute(USER_TO_CONFIRM_REG_ATTRIBUTE_NAME));
        if(userToRegisterOptional.isPresent()) {
            User user = (User) userToRegisterOptional.get();
            if(userService.checkIfUserIsValidForRegistration(user.getLogin(),user.getEmail())) {
                userService.registerUser(user);
                session.removeAttribute(USER_TO_CONFIRM_REG_ATTRIBUTE_NAME);
                model.addAttribute("registrationResult", true);
            } else {
                model.addAttribute("registrationResult",false);
            }
        } else {
            model.addAttribute("registrationResult",false);

        }
        return "loginPage";
    }

    @PostMapping("/changeLogin")
    public String changeLogin(Model model, @RequestParam String newLogin, Authentication authentication,
                                    RedirectAttributes redirectAttributes) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if(!userService.checkIfUserWithSuchLoginIsAlreadyPresent(newLogin)) {
            if(userDataValidator.validateUserChangeLoginData(newLogin)) {
                try {
                    userService.updateUserLogin(newLogin,securityUser.getUsername());
                    securityUser.setUsername(newLogin);
                } catch (CustomServiceException e) {
                    logger.error(e.getMessage());
                    redirectAttributes.addFlashAttribute("error",e.getMessage());
                }
            } else {
                redirectAttributes.addFlashAttribute("error",INVALID_DATA_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",USER_ALREADY_REGISTERED_ERROR);
        }
        return "redirect:/";
    }

    @PostMapping("/changeEmail")
    public String changeEmail(Model model, @RequestParam String newEmail, Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if(!userService.checkIfUserWithSuchEmailIsAlreadyPresent(newEmail)) {
            if(userDataValidator.validateUserChangeEmailData(newEmail)) {
                try {
                    userService.updateUserEmail(newEmail,securityUser.getEmail(),securityUser.getUsername());
                    securityUser.setEmail(newEmail);
                } catch (CustomServiceException e) {
                    logger.error(e.getMessage());
                    redirectAttributes.addFlashAttribute("error",e.getMessage());
                }
            } else {
                redirectAttributes.addFlashAttribute("error",INVALID_DATA_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",USER_ALREADY_REGISTERED_ERROR);
        }
        return "redirect:/";
    }

    @PostMapping("/changePassword")
    public String changePassword(Model model,@RequestParam String oldPassword, @RequestParam String newPassword,
                              @RequestParam String newPasswordRepeated, Authentication authentication,
                              RedirectAttributes redirectAttributes) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        if(newPassword.equals(newPasswordRepeated) && passwordEncoder.matches(oldPassword,securityUser.getPassword())) {
            if(userDataValidator.validateUserChangePasswordData(newPassword)) {
                try {
                    userService.updateUserPassword(encodedNewPassword,securityUser.getUsername());
                    securityUser.setPassword(encodedNewPassword);
                } catch (CustomServiceException e) {
                    logger.error(e.getMessage());
                    redirectAttributes.addFlashAttribute("error",e.getMessage());
                }
            } else {
                redirectAttributes.addFlashAttribute("error",INVALID_DATA_ERROR);
            }
        } else {
            redirectAttributes.addFlashAttribute("error",PASSWORD_REPEATED_INCORRECTLY);
        }
        return "redirect:/";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users",gson.toJson(userService.getAllUsers()));
        model.addAttribute("roleTypes",RoleType.values());
        return "users";
    }

    @PostMapping("/changeActivityStatus")
    public String changeUserActivityStatus(Model model,@RequestParam Long user_id) {
        userService.updateUserActivityStatus(user_id);
        return "redirect:/users";
    }

    @PostMapping("/changeUserAuthority")
    public String changeUserAuthority(Model model,@RequestParam Long user_id) {
        userService.updateUserAuthority(user_id);
        return "redirect:/users";
    }

    @PostMapping("/sortUsers")
    public String sortUsers(Model model, @RequestParam(required = false) String sortFormSelect) {
        String filteredUsers;
        if(sortFormSelect == null || sortFormSelect.equals("noneSelected")) {
            filteredUsers = gson.toJson(userService.getAllUsers());
            model.addAttribute("error", EmployeeClientsOrdersController.FILTER_TYPE_NOT_SELECTED);

        } else if(sortFormSelect.equals("byLogin")) {
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByLogin());
        } else if(sortFormSelect.equals("byEmail")) {
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByEmail());
        } else if(sortFormSelect.equals("byRoleDesc")) {
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByRoleDesc());
        } else if(sortFormSelect.equals("byRoleAsc")) {
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByRoleAsc());
        } else if(sortFormSelect.equals("byActivityDesc")) {
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByActivityDesc());
        } else if(sortFormSelect.equals("byActivityAsc")){
            filteredUsers = gson.toJson(userService.getAllUsersAndOrderByActivityAsc());
        } else {
            filteredUsers = gson.toJson(userService.getAllUsers());
        }
        model.addAttribute("users",filteredUsers);
        model.addAttribute("roleTypes",RoleType.values());
        return "users";
    }

    @PostMapping("/searchUsers")
    public String searchUsers(Model model,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String role,
                              @RequestParam String activityStatus) {
        if(role == null) {
            role = "";
        }
        List<User> filteredUsers = userService.getFilteredUsers("%"+name+"%","%"+email+"%","%"+role+"%");
        if(activityStatus.equals(ONLY_ACTIVE_USERS)) {
            filteredUsers = filteredUsers.stream().filter(User::isActive).collect(Collectors.toList());
        } else if(activityStatus.equals(ONLY_INACTIVE_USERS)) {
            filteredUsers = filteredUsers.stream().filter(user -> !user.isActive()).collect(Collectors.toList());
        }
        if(filteredUsers.size() == 0) {
            model.addAttribute("error",NONE_USERS_FOUND_SEARCH_ERROR);
        }
        model.addAttribute("users",gson.toJson(filteredUsers));
        model.addAttribute("roleTypes",RoleType.values());
        return "users";
    }
}
