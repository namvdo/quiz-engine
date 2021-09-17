package engine.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import engine.domain.QuizCompletion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCompletionDto {
    private LocalDateTime completedAt;
    @JsonIgnore
    private boolean isCorrect;

    private int id;

    @JsonIgnore
    private int userId;

    public QuizCompletionDto(QuizCompletion quizCompletion) {
        this.id = quizCompletion.getQuizId();
        this.completedAt = quizCompletion.getCompletedAt();
        this.isCorrect = quizCompletion.isCorrect();
        this.userId = quizCompletion.getUserId();
    }
}
