package cst438.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByUserId(int userId);

  User findByUserName(String userName);

  @Query(value = "SELECT * from user u WHERE u.user_id = ?1", nativeQuery = true)
  User findUserByIdAndPassword(int userId);
}
