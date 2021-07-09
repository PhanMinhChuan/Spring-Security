package zuka.cloud.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import zuka.cloud.demo.model.Role;
import zuka.cloud.demo.model.User;
import zuka.cloud.demo.repository.RoleRepository;
import zuka.cloud.demo.repository.UserRepository;

@SpringBootApplication
public class MainApplication implements CommandLineRunner{

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("123"));
        userRepository.save(user);

        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_ADMIN");
        role.setUsername(user.getUsername());
        role.setUserId(user.getId());
        roleRepository.save(role);
    }
}

