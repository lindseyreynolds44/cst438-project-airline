package cst438;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;

  @Bean
  AuthenticationProvider authentiationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return provider;

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    // regular web requests
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/searchFlights/passengers")
        .hasAuthority("user")
        .antMatchers("/**")
        .permitAll()
        .and()
        .formLogin();
    // rest requests
    http.csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/api/makeReservation")
        .hasAuthority("vendor")
        .antMatchers("/api/**")
        .permitAll()
        .and()
        .httpBasic();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // TODO Auto-generated method stub

  }



}
