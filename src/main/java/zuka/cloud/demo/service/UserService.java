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
        /*User user = userRepository.findByUsername(username);
        if(!"admin".equalsIgnoreCase(user.getUsername())) throw new UsernameNotFoundException("User name not found");
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);
        CustomUserDetails customUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), authorities);
        return customUserDetails;*/
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
        //List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //grantedAuthorities.add(new SimpleGrantedAuthority(UserRepo.getRole()));
    }

    // JWTAuthenticationFilter sẽ sử dụng hàm này
    @Transactional
    public UserDetails loadUserById(int id) {
        /*User user = userRepository.findById(id);
        if (user == null) {
            System.out.println("NULL USER");
        }*/
        User user = userRepository.findById(id);/*.orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );*/
        return new CustomUserDetails(user);
    }
}
