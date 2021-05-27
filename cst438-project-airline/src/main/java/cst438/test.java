package cst438;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class test {

  @Value("${test}")
  String testEnvVar;

  @GetMapping("/")
  public String home() {
    return testEnvVar;
  }

  public static void main(String[] args) {
    SpringApplication.run(test.class, args);
  }

}
