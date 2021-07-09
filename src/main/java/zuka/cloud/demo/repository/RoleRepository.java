package zuka.cloud.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zuka.cloud.demo.model.Role;
import zuka.cloud.demo.model.User;

import java.util.List;

public interface RoleRepository extends JpaRepository <Role, Integer> {
    List<Role> findByUsername(String username);
}
