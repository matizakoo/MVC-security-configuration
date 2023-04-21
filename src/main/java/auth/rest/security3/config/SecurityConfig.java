package auth.rest.security3.config;

import auth.rest.security3.config.jwt.*;
import auth.rest.security3.repository.UsersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfig {
    private final UsersRepository usersRepository;
    private final JwtGenerator jwtGenerator;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain securityFilterChainOne(HttpSecurity http) throws Exception {
        return http
                .securityMatcher(new AntPathRequestMatcher("/user/**", null, true))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/users/user").permitAll()
//                        .requestMatchers("/user/users/loginpage").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/user/users/auth").permitAll()
                        .requestMatchers("/user/users/**").authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
//                .formLogin(login -> login
//                        .loginPage("/user/users/loginpage")
//                        .loginProcessingUrl("/user/users/auth").permitAll()
//                        .successHandler((request, response, authentication) ->
//                                response.sendRedirect("/user/users/auth"))
//                )
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain securityFilterChainTwo(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .securityMatcher(new AntPathRequestMatcher("/admin/**", null, true))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/admin", "/admin/loginpage", "/admin/auth").permitAll()
                        .requestMatchers("/admin/**").authenticated().anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(login -> login
                        .loginPage("/admin/loginpage").permitAll()
//                        .successHandler(new CustomAuthHeader(jwtGenerator))
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(new ClearJwtCookie(CustomAuthHeader.AUTHORIZATION_HEADER)))
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

//    @Bean
//    @Order(3)
//    public SecurityFilterChain securityFilterChainSwagger(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html",
//                                "/swagger-resources/**"
//                ).permitAll())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .formLogin(login -> login
//                        .successHandler(new CustomAuthHeader(jwtGenerator)))
//                .logout(logout -> logout
//                        .logoutSuccessHandler(new ClearJwtCookie(CustomAuthHeader.AUTHORIZATION_HEADER)))
//                .authenticationProvider(authenticationProvider())
//                .build();
//    }

    //    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .securityMatcher("/jwt/*")
//                .authorizeHttpRequests().anyRequest().permitAll()
//                .and()
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(authenticationProvider);
    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf().disable()
//                .securityMatcher("/*")
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/logout").permitAll()
//                        .requestMatchers("/login").permitAll())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .logout()
//                .and()
//                .formLogin()
//                .and()
//                .build();
//    }



//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChainThree(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/**",
//                                "/v3/api-docs/**",
//                                "/swagger-ui.html",
//                                "/swagger-resources/**"
//                ).authenticated()
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/logout").permitAll())
//                .formLogin(login -> login
//                        .defaultSuccessUrl("/z"))
//                .logout(logout -> logout
//                        .logoutSuccessUrl("/z").permitAll()
//                        .logoutSuccessHandler(((request, response, authentication) -> {
//                            SecurityContextHolder.clearContext();
//                            Cookie cookiez = new CookieService("jwt", null, false, false, "/", 0);
//                            Cookie[] cookies = request.getCookies();
//                            if(cookies != null) {
//                                for(Cookie cookie: cookies) {
//                                    cookie.setValue(null);
//                                    cookie.setMaxAge(0);
//                                    response.addCookie(cookie);
//                                }
//                            }
//                            response.addCookie(cookiez);
//                            response.setStatus(HttpServletResponse.SC_OK);
//                        })))
//                .authenticationProvider(authenticationProvider())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .build();
//    }



//    @Bean
//    @Order(1)
//    public SecurityFilterChain securityFilterChainTwo(HttpSecurity http) throws Exception {
//        return http
//                .securityMatcher("/admin/**")
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/admin/admin").permitAll()
//                        .requestMatchers("/admin/**").authenticated())
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable)
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
////                .addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .httpBasic(Customizer.withDefaults())
//                .authenticationProvider(authenticationProvider())
//                .build();
//    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChainThree(HttpSecurity http) throws Exception {
//        return http
//                .csrf(AbstractHttpConfigurer::disable)
//                .securityMatcher("/")
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/**").permitAll()
//                        .requestMatchers("/login").permitAll()
//                        .requestMatchers("/logout").permitAll()
//                )
//                .formLogin()
//                .and()
//                .logout()
//                .and()
//                .authenticationProvider(authenticationProvider())
//                .build();
//    }

//    @Bean
//    @Order(2)
//    public SecurityFilterChain securityFilterChainThree(HttpSecurity http) throws Exception {
//        return http
//                .securityMatcher("/")
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/swagger-ui/**"
////                                ,"/v3/api-docs/**",
////                                "/swagger-ui.html",
////                                "/swagger-resources/**"
//                        ).authenticated())
//                .formLogin()
//                .and()
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .logoutSuccessUrl("/login")
//                        .logoutSuccessHandler(((request, response, authentication) -> {
//                            SecurityContextHolder.clearContext();
//                            Cookie[] cookies = request.getCookies();
//                            if(cookies != null) {
//                                for(Cookie cookie: cookies) {
//                                    cookie.setValue(null);
//                                    cookie.setMaxAge(0);
//                                    response.addCookie(cookie);
//                                }
//                            }
//                            response.setStatus(HttpServletResponse.SC_OK);
//                        }))
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID"))
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(AbstractHttpConfigurer::disable)
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

    @Bean
    public JwtAuthenticationFilter authenticationFilter(){
        return new JwtAuthenticationFilter();
    }

//    @Bean
//    public JwtAuthorizationFilter authorizationFilter(){
//        return new JwtAuthorizationFilter();
//    }

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
