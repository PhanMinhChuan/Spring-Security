package zuka.cloud.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;


public class CustomUserDetails implements UserDetails {

    private int id;
    private String userName;
    private String password;
    private String role;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    public int getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    //Tài khoản không hết hạn
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    //Tài khoản không bị khóa
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    //Thông tin đăng nhập không hết hạn
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    //Được kích hoạt
    public boolean isEnabled() {
        return true;
    }

}
