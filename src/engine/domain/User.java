package engine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import engine.annotation.ExtendedEmailValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    @ExtendedEmailValidator
    private String email;
    @Length(min = 5)
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
