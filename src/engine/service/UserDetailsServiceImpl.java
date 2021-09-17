package engine.service;

import engine.adapter.UserAdapter;
import engine.domain.MyUserDetail;
import engine.domain.User;
import engine.dto.UserDto;
import engine.exception.UserAlreadyRegisteredException;
import engine.repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepo userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserAdapter userAdapter;

    public UserDetailsServiceImpl(UserRepo userRepo, UserAdapter userAdapter) {
        this.userRepo = userRepo;
        this.userAdapter = userAdapter;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepo.getUserByEmail(email);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Not found user");
        } else {
            User user = userOptional.get();
            return new MyUserDetail(user);
        }
    }

    public void saveUser(UserDto userDto) throws UserAlreadyRegisteredException {
        if (!doesUserExist(userDto.getEmail())) {
            String password = userDto.getPassword();
            userDto.setPassword(passwordEncoder.encode(password));
            User user = userAdapter.getUserFromUserDto(userDto);
            User savedUser = this.userRepo.save(user);
            log.info("Saved user: {}", savedUser);
        } else {
            throw new UserAlreadyRegisteredException("Already registered");
        }
    }

    private boolean doesUserExist(String email) {
        return this.userRepo.getUserByEmail(email).isPresent();
    }

    public Optional<User> getUserByEmail(String email) {
        return this.userRepo.getUserByEmail(email);
    }

}
