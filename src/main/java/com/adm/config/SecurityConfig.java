package com.adm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

import javax.sql.DataSource;

/**
 * Created by Milan_Verheij on 16-08-16.
 *
 * Simple security configuration (pg 248)
 *
 *  @EnableWebMvcSecurity is deprecated en is nu @EnableWebSecurity die zelf ziet wat er nodig is
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                 .loginPage("/login")
                 .defaultSuccessUrl("/loginSuccess")
                .and()
                 .logout()
                  .logoutUrl("/logout")
                   .logoutSuccessUrl("/")
                .and()
                 .rememberMe()
                  .tokenRepository(new InMemoryTokenRepositoryImpl())
                  .tokenValiditySeconds(2419200)
                  .key("harrieKey")
                .and()
                 .httpBasic()
                  .realmName("Harrie")
                .and()
                .authorizeRequests()
//                 .antMatchers("/").authenticated()
//                 .antMatchers(HttpMethod.POST, "/klanten/register").authenticated() //TODO: Veranderen
                 .anyRequest().permitAll();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                 .withUser("user").password("password").roles("USER")
//                .and()
//                 .withUser("Harrie").password("1234").roles("USER")
//                .and()
//                 .withUser("admin").password("password").roles("ADMIN");
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                .jdbcAuthentication()
                 .dataSource(dataSource)
                 .usersByUsernameQuery(
                         "select email, password, klantActief " +
                         "from Klant where email=?")
                 .authoritiesByUsernameQuery(
                         "select email, klantRol from Klant where email=?");
    }
}
