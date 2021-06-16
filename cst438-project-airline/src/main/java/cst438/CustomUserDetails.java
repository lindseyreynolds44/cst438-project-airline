package cst438;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import cst438.domain.User;


public class CustomUserDetails implements UserDetails {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private User user;

  public CustomUserDetails(User user) {
    super();
    this.user = user;
  }

  public CustomUserDetails() {}

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(user.getRole()));
  }

  @Override
  public String getPassword() {
    // TODO Auto-generated method stub
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    // TODO Auto-generated method stub
    return user.getUserName();
  }

  @Override
  public boolean isAccountNonExpired() {
    // TODO Auto-generated method stub
    return true; // hardcoded since we aren't really dealing with this in the db.
  }

  @Override
  public boolean isAccountNonLocked() {
    // TODO Auto-generated method stub
    return true; // hardcoded since we aren't really dealing with this in the db.
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // TODO Auto-generated method stub
    return true; // hardcoded since we aren't really dealing with this in the db.
  }

  @Override
  public boolean isEnabled() {
    // TODO Auto-generated method stub
    return true; // hardcoded since we aren't really dealing with this in the db.
  }



}
