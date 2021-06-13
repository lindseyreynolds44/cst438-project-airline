package cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import cst438.domain.User;
import cst438.domain.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

    User user = userRepository.findByUserName(userName);

    if (user == null) {
      throw new UsernameNotFoundException("User Not Found");
    }
    return new cst438.CustomUserDetails(user);
  }

}
