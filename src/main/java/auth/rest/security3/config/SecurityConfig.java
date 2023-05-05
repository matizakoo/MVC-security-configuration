package auth.rest.security3.config;

import auth.rest.security3.config.bruteforce.BruteForceService;
import auth.rest.security3.config.jwt.*;
import auth.rest.security3.controller.HttpsController;
import auth.rest.security3.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfig {
    private final UsersRepository usersRepository;

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainOne(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(new AntPathRequestMatcher("/user/**", null, false))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/users/user").permitAll()
                        .requestMatchers("/user/users/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChainTwo(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(new AntPathRequestMatcher("/admin/**", null, false))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/admin", "/admin/loginpage", "/admin/auth", "/admin/button", "/admin/successlogout", "/admin/logout").permitAll()
                        .requestMatchers("/admin/user").hasAuthority("USER")
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(login -> login
                        .loginPage("/admin/loginpage").permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/admin/logout")
                        .logoutSuccessHandler(new ClearJwtCookie(CustomAuthHeader.AUTHORIZATION_HEADER))
                )
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain securityFilterChainHttps(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(new AntPathRequestMatcher("/https/**", null, false))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/https/**").permitAll()
                )
                .requiresChannel(https -> https
                        .requestMatchers("/https/**").requiresSecure())
                .build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain securityFilterChainLdap(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .and()
                .build();
    }

//    @Autowired
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                .userDnPatterns("uid={0},ou=people")
//                .groupSearchBase("ou=groups")
//                .contextSource()
//                .url("ldap://localhost:8389/dc=springframework,dc=org")
//                .and()
//                .passwordCompare()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .passwordAttribute("userPassword");
//    }

//    @Autowired
//    public void configureLdap(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                .userDnPatterns("ou=people")
//                .contextSource()
//                .url("ldap://10.5.110.200:4260/dc=sso,dc=so")
//                .and()
//                .passwordCompare()
//                .passwordEncoder(NoOpPasswordEncoder.getInstance())
//                .passwordAttribute("password123");
//    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return usersRepository::findByUsername;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
