package zuka.cloud.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import zuka.cloud.demo.model.Role;
import zuka.cloud.demo.model.User;
import zuka.cloud.demo.repository.RoleRepository;
import zuka.cloud.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service(("userDetailsService"))
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user;
        try {
            user = userRepository.findByUsername(name);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new RuntimeException("Database error.");
        }

        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        return buildUserFromUserEntity(user);
    }

    private org.springframework.security.core.userdetails.User buildUserFromUserEntity(User user) {
        // convert model user to spring security user
        String username               = user.getUsername();
        String password               = user.getPassword();
        boolean enable                = true;
        boolean accountNonExpired     = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked      = true;

        List<GrantedAuthority> authorities = new ArrayList<>();
        try {
            for (Role role : roleRepository.findByUsername(username)) {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        org.springframework.security.core.userdetails.User springUser = new org.springframework.security.core.userdetails.User(
                username,
                password,
                enable,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);
        return springUser;
    }
}
