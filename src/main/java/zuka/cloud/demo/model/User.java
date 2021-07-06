package zuka.cloud.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import zuka.cloud.demo.repository.UserRepository;

import javax.persistence.*;
import java.io.Serializable;

/*class UserId implements Serializable {

    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "role", nullable = false)
    private String role;

    public UserId(String role, int id) {
        this.role = role;
        this.id = id;
    }

    public UserId() {
    }
}*/

@Entity
@Table(name = "user")
//@IdClass(UserId.class)
//@ApiModel(value = "User model")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    //@Id
    @Column(name = "role", length = 100, nullable = false)
    private String role;

    @Column(name = "username", length = 32, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

}
