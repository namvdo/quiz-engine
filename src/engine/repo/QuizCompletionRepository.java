package engine.repo;

import engine.domain.QuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizCompletionRepository extends JpaRepository<QuizCompletion, Integer> {
    Page<QuizCompletion> getQuizCompletionByUserId(int userId, Pageable pageable);
}
