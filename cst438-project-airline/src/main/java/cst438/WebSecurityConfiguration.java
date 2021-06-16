/**
 * LINK:
 * https://stackoverflow.com/questions/35761181/securing-only-rest-controller-with-spring-security-custom-token-stateless-while
 */
package cst438;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
  public UserDetailsService userDetailsService() {
    return super.userDetailsService();
  }

  @Bean
  AuthenticationProvider authentiationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(new BCryptPasswordEncoder());
    return provider;

  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf()
        .disable() // allows post request
        .authorizeRequests()
        .antMatchers("/searchFlights/passengers", "/reservations")
        .hasAuthority("user") // allows only authenticated users to view /searchFlights/passengers
        // .antMatchers("/api/makeReservation", "/api/cancelReservation", "/api/getAllReservations")
        // .hasAuthority("vendor") // allows only authenticated vendors to use /api/makeReservation
        .antMatchers("/**")
        .permitAll() // allows all others pages to not require auth
        .and()
        .formLogin(); // displays login page for browser requests
    // .and()
    // .httpBasic(); // allows for http basic auth for http requests

  }

}
