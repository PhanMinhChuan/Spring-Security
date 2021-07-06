package zuka.cloud.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "role", length = 100, nullable = false)
    private String role;

    @Column(name = "username", length = 32, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

}
