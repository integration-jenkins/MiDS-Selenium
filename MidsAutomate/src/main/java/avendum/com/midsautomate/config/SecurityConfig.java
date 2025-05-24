package avendum.com.midsautomate.config;


import avendum.com.midsautomate.service.CustomUserDetailsService;
import avendum.com.midsautomate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    /*
//    //This is the service that loads user-specific data(like username and password)
//     */
//    @Autowired
//    private CustomUserDetailsService userDetailsService;


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
//        configuration.setAllowedMethods(Arrays.asList("*"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }

// @Bean
// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////     http.csrf(csrf -> csrf.disable())
//     http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
//             .csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/auth/**", "auth/login").permitAll()
//                     .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                 .anyRequest().authenticated()
//             )
//             .sessionManagement(session -> session
//                 .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//             );
//
//     http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil, userDetailsService), UsernamePasswordAuthenticationFilter.class);
//     return http.build();
// }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}


 @Configuration
 @EnableWebSecurity
 public class SecurityConfig implements WebMvcConfigurer {
     @Autowired
     private JwtUtil jwtUtil;
     @Autowired
     private CustomUserDetailsService userDetailsService;

     public SecurityConfig() {
     }

     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests((auth) -> {
             ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)auth.requestMatchers(new String[]{"/", "/auth/**", "/index.html", "/static/**", "/login", "/dpr", "/dprform", "/dashboard", "/signup", "/midstest", "/testing", "/mwdpr", "/testreports", "/download-report", "/basic-test-report", "/hypnotic-loader", "/favicon.ico", "/manifest.json", "/asset-manifest.json", "/error", "/js/**", "/css/**", "/images/**"})).permitAll().requestMatchers(HttpMethod.OPTIONS, new String[]{"/**"})).permitAll().anyRequest()).authenticated();
         }).sessionManagement((session) -> {
             session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
         }).exceptionHandling((exceptions) -> {
             exceptions.authenticationEntryPoint((request, response, authException) -> {
                 if (!response.isCommitted()) {
                     response.sendError(401, "Unauthorized");
                 }

             });
         });
         http.addFilterBefore(this.jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
         return (SecurityFilterChain)http.build();
     }

     @Bean
     public JwtAuthenticationFilter jwtAuthenticationFilter() {
         return new JwtAuthenticationFilter(this.jwtUtil, this.userDetailsService);
     }

     @Bean
     public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
         return authenticationConfiguration.getAuthenticationManager();
     }

     @Bean
     public PasswordEncoder passwordEncoder() {
         return new BCryptPasswordEncoder();
     }

     public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/{path:[^\\.]*}").setViewName("forward:/index.html");
     }
 }
