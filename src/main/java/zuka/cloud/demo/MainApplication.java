package zuka.cloud.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import zuka.cloud.demo.model.User;
import zuka.cloud.demo.repository.UserRepository;

@SpringBootApplication
public class MainApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setId(1);
        user1.setUsername("admin");
        user1.setPassword(passwordEncoder.encode("123"));
        user1.setRole("ROLE_ADMIN");
        userRepository.save(user1);

        User user2 = new User();
        user2.setId(2);
        user2.setUsername("user");
        user2.setPassword(passwordEncoder.encode("123"));
        user2.setRole("ROLE_USER");
        userRepository.save(user2);
    }
}

