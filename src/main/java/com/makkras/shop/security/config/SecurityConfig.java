package com.makkras.shop.security.config;

import com.makkras.shop.entity.RoleType;
import com.makkras.shop.security.CustomUserDetailsService;
import com.makkras.shop.security.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService, AccessDeniedHandler accessDeniedHandler,
                          CustomAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .anonymous().authorities(RoleType.GUEST.getRoleName())
                .and()
                .authorizeHttpRequests()
                .antMatchers("/","/pictures/**","/catalog","/aboutUs","/js/**",
                        "/css/**","/search","/sortCatalog","/favicon.ico","/prod_pics/**")
                .permitAll()
                .antMatchers("/employeeMain","/clientsOrders","/stockRefillOrders","/statistics","/finances",
                        "/suppliers","/users","/updateProductData","/addProduct","/sortAllProducts",
                        "/searchAllProducts","/clientsOrders","/confirmClOrder","/filterClOrders",
                        "/suppliers", "/updateSupplierData", "/addSupplier", "/sortSuppliers",
                        "/searchSuppliers", "/searchClOrders","/addProductToStockRefillOrder",
                        "/removeProductFromStockRefillOrder","/addSROrder","/completeSROrder")
                .hasAuthority("EMPLOYEE")
                .antMatchers("/logout","/changeLogin","/changePassword","/changeEmail","/addToBasket",
                        "/showBasket","/alterOrder","/orderProducts")
                .hasAnyAuthority("EMPLOYEE","CLIENT")
                .antMatchers("/register","/regconfirm")
                .hasAuthority("GUEST")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .usernameParameter("login")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler)
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true);
                //.httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
        //return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }
}
