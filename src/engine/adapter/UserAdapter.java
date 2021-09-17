package engine.adapter;

import engine.domain.User;
import engine.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {
    public User getUserFromUserDto(UserDto userDto) {
        return new User(userDto.getEmail(), userDto.getPassword());
    }
}
