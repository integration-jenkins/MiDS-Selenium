package com.avendum.midsautomate.config;

import com.avendum.midsautomate.service.CustomUserDetailsService;
import com.avendum.midsautomate.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .cors().and()
                    .csrf().disable()
                    .authorizeRequests()
                .antMatchers(
                        "/",
                        "/index.html",
                        "/favicon.ico",
                        "/manifest.json",
                        "/asset-manifest.json",
                        "/static/**",
                        "/js/**",
                        "/css/**",
                        "/images/**",
                        "/auth/**",
                        "/login",
                        "/dashboard",
                        "/signup",
                        "/automation-testing/page-performance-dashboard",
                        "/setting/feedback",
                        "/page-report-visualization",
                        "/reports/page-render-report",
                        "/reports/download-report",
                        "/reports/mids-tests-report",
                        "/automation-testing/mw-dpr-track",
                        "/automation-testing",
                        "/automation-testing/mids-test",
                        "/profile-management/profile-modify",
                        "/automation-testing/basic-test",
                        "/api/images/**",
                        "/add-mids-test",
                        "/auth/login",
                        "api/dismantleTest/workflow",
                        "api/dismantleTest/bulkTest",
                        "api/dismantleTest/testResult",
                        "api/dismantleTest/viewReport",
                        "api/trafficShifting/workflow",
                        "api/trafficShifting/testResult",
                        "api/trafficShifting/testCode",
                        "api/trafficShifting/testBulkUploadSheet",
                        "api/trafficShifting/bulkUploadTest",
                        "api/trafficShifting/getUsers",
                        "api/trafficShifting/setUser",
                        "/dismantle/dashboard",
                        "/dismantle/dashboard/form",
                        "/dismantle/dashboard/form/testReport",
                        "/dismantle/dashboard/sheetValidation",
                        "/trafficShifting/dashboard"
                ).permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> {
                    if (!response.isCommitted()) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    }
                });

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil, userDetailsService) {
            @Override
            protected boolean shouldNotFilter(javax.servlet.http.HttpServletRequest request) {
                String path = request.getServletPath();
                return path.startsWith("/auth/") || path.startsWith("/login") || path.startsWith("/static/");
            }
        };
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
    }
}