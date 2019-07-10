package gg.gamello.user.configuration;

import gg.gamello.user.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.PublicKey;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PublicKey jwtKey;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/")
                    .permitAll()
                .antMatchers( "/confirm/**", "/recover", "/validate/**", "/api/**", "/id/**", "/special/**")
                    .permitAll()
                .antMatchers("/", "/change/**")
                    .authenticated();

        http
                .formLogin()
                    .disable()
                .logout()
                    .disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtKey));

        http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.POST, "/")
                .antMatchers( "/confirm/**", "/recover", "/validate/**", "/api/**", "/id/**", "/special/**");
    }
}
