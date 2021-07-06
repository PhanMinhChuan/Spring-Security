package zuka.cloud.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zuka.cloud.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Integer> {
    User findByUsername(String username);

    User findById(int id);
}
