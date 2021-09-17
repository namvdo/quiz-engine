package engine.adapter;

import engine.domain.Quiz;
import engine.dto.QuizDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class QuizAdapter {
    public Quiz getQuizFromQuizDto(QuizDto quizDto) {
        Quiz quiz = new Quiz();
        quiz.setId(quizDto.getId());
        quiz.setUserId(quizDto.getUserId());
        quiz.setAnswer(quizDto.getAnswer() == null ? new ArrayList<>() : quizDto.getAnswer());
        quiz.setTitle(quizDto.getTitle());
        quiz.setOptions(quizDto.getOptions());
        quiz.setText(quizDto.getText());
        return quiz;
    }

    public Optional<QuizDto> getQuizDtoOptionalFromQuizOptional(Optional<Quiz> quizOptional) {
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            QuizDto quizDto = new QuizDto(quiz.getId(),
                    quiz.getTitle(), quiz.getText(), quiz.getOptions(),
                    quiz.getAnswer(), quiz.getUserId());
            return Optional.of(quizDto);
        }
        return Optional.empty();
    }

    public List<QuizDto> getListQuizDtoFromListQuiz(List<Quiz> quizzes) {
        return quizzes.stream()
                .map(QuizDto::new)
                .collect(Collectors.toList());
    }
}
