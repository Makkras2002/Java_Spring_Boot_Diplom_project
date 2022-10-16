package com.makkras.shop.controller;

import com.makkras.shop.entity.RoleType;
import com.makkras.shop.entity.User;
import com.makkras.shop.entity.UserRole;
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

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

@Controller
public class UserController {

    private static final  String USER_ALREADY_REGISTERED_ERROR = "Пользователь с таким логином или почтой уже зарегистрирован!";
    private static final  String URL_TO_CONFIRM_REGISTRATION_BY_EMAIL = "/regconfirm";
    private static final String APP_HOST = "http://localhost:8080";
    private static final String USER_TO_CONFIRM_REG_ATTRIBUTE_NAME = "userAwaitingEmailConfirmationToRegister";
    private static Logger logger = LogManager.getLogger();
    private final UserService userService;
    private final UserDataValidator userDataValidator;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Autowired
    public UserController(CustomUserService userService, CustomUserDataValidator userDataValidator,
                          CustomMailService mailService) {
        this.userService = userService;
        this.userDataValidator = userDataValidator;
        this.passwordEncoder = new BCryptPasswordEncoder(12);
        this.mailService = mailService;
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

}
