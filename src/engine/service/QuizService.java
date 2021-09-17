package engine.service;

import engine.domain.ClientAnswer;
import engine.domain.Quiz;
import engine.dto.QuizDto;
import engine.exception.InvalidAnswerException;
import engine.exception.QuizNotFoundException;
import engine.paging_model.QuizCompletionPageable;
import engine.paging_model.QuizPageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface QuizService {

    Quiz saveQuiz(int userId, QuizDto quizDto);

    Optional<QuizDto> getQuiz(int id);

    boolean solveQuiz(int quizId, ClientAnswer clientAnswer) throws InvalidAnswerException, QuizNotFoundException;

    void saveQuizCompletion(int userId, int quizId) throws QuizNotFoundException;


    void deleteQuiz(int quizId) throws QuizNotFoundException;

    boolean isQuizOwner(int userId, int quizId);

    boolean isQuizPresent(int quizId);

    QuizCompletionPageable getQuizCompletion(int userId, int pageNo, int offset);

    QuizPageable getAllQuizzes(int pageNo, int offset, String sortedBy);

}