package auth.rest.security3.config;

import auth.rest.security3.config.ldap.JwtLdapAuthorizationAttackFilter;
import auth.rest.security3.config.ldap.JwtLdapAuthenticationFilter;
import auth.rest.security3.repository.UsersRepository;
import auth.rest.security3.service.AttackService;
import auth.rest.security3.service.AttackServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfig {
    private final UsersRepository usersRepository;
    private final AttackService attackService;

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

//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChainTwo(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .securityMatcher(new AntPathRequestMatcher("/admin/**", null, false))
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/admin", "/admin/loginpage", "/admin/auth", "/admin/button", "/admin/successlogout", "/admin/logout").permitAll()
//                        .requestMatchers("/admin/user").hasAuthority("USER")
//                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(login -> login
//                        .loginPage("/admin/loginpage").permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/admin/logout")
//                        .logoutSuccessHandler(new ClearJwtCookie(CustomAuthHeader.AUTHORIZATION_HEADER))
//                )
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

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
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(new AntPathRequestMatcher("/ldap/**", null, false))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/ldap/findby/**").permitAll()
                        .requestMatchers("/ldap/auth").permitAll()
                        .requestMatchers("/ldap/login").permitAll()
                        .requestMatchers("/ldap/findbyemail/**").permitAll()
                        .requestMatchers("/ldap/jwt/**").permitAll()
                        .requestMatchers("/ldap/omno/**").permitAll()
                        .requestMatchers("/ldap/user").hasAuthority("USER")
                        .requestMatchers("/ldap/admin").hasAuthority("ADMIN")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtLdapAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(jwtLdapAuthenticationAttackFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public JwtLdapAuthenticationFilter jwtLdapAuthenticationFilter() { return new JwtLdapAuthenticationFilter(); }

//    @Bean
//    public JwtLdapAuthorizationAttackFilter jwtLdapAuthenticationAttackFilter() { return new JwtLdapAuthorizationAttackFilter(); }


    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(authenticationProvider);
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
