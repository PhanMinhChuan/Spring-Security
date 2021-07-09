package zuka.cloud.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.MappedSuperclass;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class CustomAuditingConfiguration {

    @Bean
    public AuditorAware<String> createAuditorProvider() {
        return new SecurityAuditor();
    }

    @Bean
    public AuditingEntityListener createAuditingListener() {
        return new AuditingEntityListener();
    }

    public static class SecurityAuditor implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            try {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth == null || !auth.isAuthenticated()) {
                    return Optional.of("null");
                }
                UserDetails userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
                return Optional.of(userDetails.getUsername());
            } catch (Exception e) {
                //e.printStackTrace();
                return Optional.of("null");
            }
        }
    }
}
