package dev.pack.config;

import dev.pack.enums.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final AppConfig app;

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.exceptionHandling(
                (exceptionHandling) -> exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        );
        httpSecurity.authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(String.format("/api/v%d/public/**",app.APP_VERSION)).permitAll();
                            /**
                             * Comment this block of request matchers if the developing has done.
                             */
                            request.requestMatchers(String.format("/api/v%d/public/admin/**",app.APP_VERSION)).permitAll();

                            //Admin
                            request.requestMatchers(String.format("api/v%d/admin/%s/**",app.APP_VERSION,Roles.ADMIN_TKJ.name())).hasRole(Roles.ADMIN_TKJ.name());
                            request.requestMatchers(String.format("api/v%d/admin/%s/**",app.APP_VERSION,Roles.ADMIN_AKL.name())).hasRole(Roles.ADMIN_AKL.name());
                            request.requestMatchers(String.format("api/v%d/admin/%s/**",app.APP_VERSION,Roles.ADMIN_TE.name())).hasRole(Roles.ADMIN_TE.name());
                            request.requestMatchers(String.format("api/v%d/admin/%s/**",app.APP_VERSION,Roles.ADMIN_TKR.name())).hasRole(Roles.ADMIN_TKR.name());

                            //Superadmin
                            request.requestMatchers(String.format("api/v%d/superadmin/**",app.APP_VERSION)).hasRole("SUPERADMIN");

//                            request.anyRequest().permitAll();
                            request.anyRequest().fullyAuthenticated();
                        }
                );
        httpSecurity.formLogin((form) -> form.loginPage("/login").permitAll());
        httpSecurity.logout((form) -> form.logoutUrl("/logout").permitAll());
        httpSecurity.sessionManagement(
                (sessionManagement) -> {
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    sessionManagement.sessionConcurrency(
                            (sessionConcurrency) -> sessionConcurrency
                                    .maximumSessions(1)
                                    .expiredUrl("/login?expired")
                    );
                }
        );
        httpSecurity.authenticationProvider(authenticationProvider);
        httpSecurity.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}