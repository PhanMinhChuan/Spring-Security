package zuka.cloud.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import zuka.cloud.demo.jwt.JwtAuthenticationFilter;
import zuka.cloud.demo.service.UserService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)          // Cung cấp userservice cho spring security
                .passwordEncoder(passwordEncoder());  // cung cấp passwordEncoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Cấu hình cho Login Form.
//        http.authorizeRequests().and()
//                .formLogin()//
//                .loginProcessingUrl("/j_spring_security_login")//
//                .loginPage("/login")//
//                .defaultSuccessUrl("/user")//
//                .failureUrl("/login?message=error")//
//                .usernameParameter("username")//
//                .passwordParameter("password")
//                // Cấu hình cho Logout Page.
//                .and()

        http
                .authorizeRequests()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .antMatchers("/user/**").hasAnyRole("USER","ADMIN")
                    //.anyRequest().authenticated()
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/403")
                    .and()
                .logout()
                    .logoutUrl("/j_spring_security_logout").logoutSuccessUrl("/login?message=logout");
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
//        http
//                .cors()
//                    .and()
//                .csrf()
//                    .disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                .authorizeRequests()
//                    .antMatchers("/login").permitAll() // Cho phép tất cả mọi người truy cập vào 2 địa chỉ này
//                    .antMatchers("/admin").hasRole("ADMIN")
//                    .antMatchers("/user").hasAnyRole("USER","ADMIN");// Tất cả các request khác đều cần phải xác thực mới được truy cập
//                    //.antMatchers("/login").permitAll() // Cho phép tất cả mọi người truy cập vào 2 địa chỉ này
//                    //.anyRequest().authenticated();
//                    //.antMatchers().authenticated();
//        // Thêm một lớp Filter kiểm tra jwt
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
///*        http
//                .cors()
//                    .and()
//                .csrf()
//                    .disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                    .and()
//                .authorizeRequests()
//                .antMatchers("/signIn").permitAll()
//                .anyRequest().authenticated();*/
//    }
}
