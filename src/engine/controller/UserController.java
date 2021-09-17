package engine.controller;

import engine.dto.UserDto;
import engine.exception.UserAlreadyRegisteredException;
import engine.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value="/api")
@Slf4j
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());
    private final UserDetailsServiceImpl userService;

    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto credential, HttpServletRequest request) {
        try {
            logger.log(Level.INFO, "credentials: {0}", credential);
            String password = credential.getPassword();
            this.userService.saveUser(credential);
            request.login(credential.getEmail(), password);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (UserAlreadyRegisteredException | ServletException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> handleInvalidRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
