package engine.adapter;

import engine.domain.QuizCompletion;
import engine.dto.QuizCompletionDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuizCompletionAdapter {
    public List<QuizCompletionDto> getQuizCompletionDtoFromQuizCompletions(List<QuizCompletion> quizCompletions) {
         return quizCompletions.stream()
                 .map(QuizCompletionDto::new)
                 .collect(Collectors.toList());
    }
}
