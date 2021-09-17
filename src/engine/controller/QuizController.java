package engine.controller;

import engine.domain.ClientAnswer;
import engine.domain.Quiz;
import engine.domain.User;
import engine.dto.QuizDto;
import engine.exception.InvalidAnswerException;
import engine.exception.QuizNotFoundException;
import engine.paging_model.QuizCompletionPageable;
import engine.paging_model.QuizPageable;
import engine.repo.QuizRepository;
import engine.service.QuizService;
import engine.service.UserDetailsServiceImpl;
import engine.view_model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api")
@Validated
public class QuizController {
    private final Logger logger = Logger.getLogger(QuizController.class.getName());
    private final QuizService quizService;
    private final UserDetailsServiceImpl userService;

    public QuizController(QuizService quizService, UserDetailsServiceImpl userService, QuizRepository quizRepository) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @PostMapping(value = "/quizzes", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuizDto> createQuiz(@Valid @RequestBody QuizDto quizDto) {
        logger.log(Level.INFO, "Quiz: {0}", quizDto);
        int curUserId = getCurrentUserId();
        if (curUserId != -1) {
            Quiz quiz = this.quizService.saveQuiz(curUserId, quizDto);
            quizDto.setId(quiz.getId());
            return ResponseEntity.ok(quizDto);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<QuizDto> getQuiz(@PathVariable @Min(0) int id) {
        Optional<QuizDto> quizOptional = this.quizService.getQuiz(id);
        return quizOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "/quizzes")
    public ResponseEntity<QuizPageable> getAllQuizzes(@RequestParam(defaultValue = "0", name = "page") int pageNo,
                                                      @RequestParam(defaultValue = "10", name = "size") int offset,
                                                      @RequestParam(defaultValue = "id") String sortedBy) {
        QuizPageable quizPageable = this.quizService.getAllQuizzes(pageNo, offset, sortedBy);
        return ResponseEntity.ok(quizPageable);
    }

    @PostMapping(value = "/quizzes/{quizId}/solve")
    public ResponseEntity<Response> solveQuiz(@RequestBody ClientAnswer answer, @PathVariable int quizId) {
        try {
            this.quizService.getQuiz(quizId).ifPresent(q -> logger.log(Level.INFO, "Solve quiz: {0}", q));
            boolean isCorrect = this.quizService.solveQuiz(quizId, answer);
            logger.log(Level.INFO, "Client answer: {0}", answer);
            logger.log(Level.INFO, "Is correct: {0}", isCorrect);

            int curUserId = getCurrentUserId();
            if (isCorrect) {
                logger.log(Level.INFO, "Save quiz result: {0}", quizId);
                logger.log(Level.INFO, "Save quiz result: {0}", answer);
                this.quizService.saveQuizCompletion(curUserId, quizId);
                return ResponseEntity.ok(Response.successResponse());
            }
            return ResponseEntity.ok(Response.failResponse());
        } catch (InvalidAnswerException e) {
            e.printStackTrace();
            return ResponseEntity.ok(Response.failResponse());
        } catch (QuizNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    @DeleteMapping("/quizzes/{quizId}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable int quizId) {
        try {
            int curUserId = getCurrentUserId();
            if (curUserId != -1) {
                logger.log(Level.INFO, "Quiz {0}", this.quizService.getQuiz(quizId).orElse(null));
                if (this.quizService.isQuizPresent(quizId) && !this.quizService.isQuizOwner(curUserId, quizId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
                this.quizService.deleteQuiz(quizId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (QuizNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/quizzes/completed")
    public ResponseEntity<QuizCompletionPageable> getQuizCompletion(@RequestParam(defaultValue = "0") int page,
                                                                    @RequestParam(defaultValue = "10") int offset) {
        int curUserId = getCurrentUserId();
        QuizCompletionPageable quizCompletion = quizService.getQuizCompletion(curUserId, page, offset);
        return ResponseEntity.ok(quizCompletion);
    }

    private int getCurrentUserId() {
        SecurityContext securityCxt = SecurityContextHolder.getContext();
        Authentication auth = securityCxt.getAuthentication();
        String username = auth.getName();
        Optional<User> userOptional = this.userService.getUserByEmail(username);
        return userOptional.map(User::getId).orElse(-1);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Void> handleInvalidRequest() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
