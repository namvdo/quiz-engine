package engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Length(min=5)
    private String password;
}
