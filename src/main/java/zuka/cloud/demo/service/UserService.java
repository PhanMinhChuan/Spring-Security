package zuka.cloud.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zuka.cloud.demo.model.CustomUserDetails;
import zuka.cloud.demo.model.User;
import zuka.cloud.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }

    // JWTAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    public UserDetails loadUserById(int id) {
        User user = userRepository.findById(id);/*orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );*/
        if (user == null) {
            throw new UsernameNotFoundException("User not found with id : " + id);
        }
        return new CustomUserDetails(user);
    }
}
