package engine.service;

import engine.adapter.QuizAdapter;
import engine.adapter.QuizCompletionAdapter;
import engine.domain.ClientAnswer;
import engine.domain.Quiz;
import engine.domain.QuizCompletion;
import engine.dto.QuizCompletionDto;
import engine.dto.QuizDto;
import engine.exception.InvalidAnswerException;
import engine.exception.QuizNotFoundException;
import engine.paging_model.QuizCompletionPageable;
import engine.paging_model.QuizPageable;
import engine.repo.QuizCompletionRepository;
import engine.repo.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepo;
    private final QuizCompletionRepository quizCompletionRepo;

    private final QuizAdapter quizAdapter;
    private final QuizCompletionAdapter quizCompletionAdapter;

    public QuizServiceImpl(QuizRepository quizRepo, QuizAdapter quizAdapter, QuizCompletionRepository quizCompletionRepo,
                           QuizCompletionAdapter quizCompletionAdapter) {
        this.quizRepo = quizRepo;
        this.quizAdapter = quizAdapter;
        this.quizCompletionRepo = quizCompletionRepo;
        this.quizCompletionAdapter = quizCompletionAdapter;
    }

    @Override
    public Quiz saveQuiz(int userId, QuizDto quizDto) {
        quizDto.setUserId(userId);
        Quiz quiz = quizAdapter.getQuizFromQuizDto(quizDto);
        return this.quizRepo.save(quiz);
    }

    @Override
    public Optional<QuizDto> getQuiz(int id) {
        Optional<Quiz> quizOptional = this.quizRepo.findById(id);
        return this.quizAdapter.getQuizDtoOptionalFromQuizOptional(quizOptional);
    }

    @Override
    public boolean solveQuiz(int quizId, ClientAnswer clientAnswer) throws InvalidAnswerException, QuizNotFoundException {
        if (clientAnswer == null) {
            throw new InvalidAnswerException("Answer is null");
        }
        if (!isQuizPresent(quizId)) {
            throw new QuizNotFoundException("Quiz not found");
        }
        Optional<Quiz> quizOptional = this.quizRepo.findById(quizId);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            int ansLen = quiz.getAnswer().size();
            List<Integer> answerFromClient = clientAnswer.getAnswer();
            if (answerFromClient == null || answerFromClient.isEmpty() && !quiz.getAnswer().isEmpty()) {
                return false;
            }
            if (ansLen < answerFromClient.size()) {
                return false;
            }
            int totalCorrect = 0;
            for (var ans : clientAnswer.getAnswer()) {
                if (quiz.getAnswer().contains(ans)) {
                    totalCorrect++;
                }
            }
            return totalCorrect == quiz.getAnswer().size();
        }
        return false;
    }

    @Override
    public void saveQuizCompletion(int userId, int quizId) throws QuizNotFoundException {
        Optional<Quiz> quizOptional = this.quizRepo.findById(quizId);
        if (quizOptional.isPresent()) {
            log.info("Found quiz with given id: {}", quizId);
            var quizCompletion = new QuizCompletion();
            Quiz quiz = quizOptional.get();
            quizCompletion.setUserId(userId);
            quizCompletion.setCorrect(true);
            quizCompletion.setCompletedAt(LocalDateTime.now());
            quizCompletion.setQuizId(quiz.getId());
            this.quizCompletionRepo.save(quizCompletion);
        } else {
            throw new QuizNotFoundException("Not found quiz");
        }
    }


    @Override
    public void deleteQuiz(int quizId) throws QuizNotFoundException {
        if (!isQuizPresent(quizId)) {
            throw new QuizNotFoundException("Not found quiz");
        }
        this.quizRepo.deleteById(quizId);
    }

    @Override
    public boolean isQuizOwner(int userId, int quizId) {
        Optional<Quiz> quizOptional = this.quizRepo.findById(quizId);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            return quiz.getUserId() == userId;
        }
        return false;
    }

    @Override
    public boolean isQuizPresent(int quizId) {
        return this.quizRepo.findById(quizId).isPresent();
    }

    @Override
    public QuizCompletionPageable getQuizCompletion(int userId, int pageNo, int offset) {
        Pageable pageable = PageRequest.of(pageNo, offset, Sort.by("completedAt").descending());
        Page<QuizCompletion> pagedResult = this.quizCompletionRepo.getQuizCompletionByUserId(userId, pageable);
        var quizCompletionPageable = new QuizCompletionPageable();
        List<QuizCompletionDto> quizCompletionDto = new ArrayList<>();
        if (pagedResult.hasContent()) {
            List<QuizCompletion> quizCompletions = pagedResult.getContent();
            quizCompletionDto = getAllQuizCompletionForUser(userId, quizCompletions);
            quizCompletionPageable.setEmpty(false);
        } else {
            quizCompletionPageable.setEmpty(true);
        }
        int totalSize = quizCompletionDto.size();
        int totalPages = totalSize / offset + 1;

        quizCompletionPageable.setContent(quizCompletionDto);
        quizCompletionPageable.setTotalElements(totalSize);
        quizCompletionPageable.setTotalPages(totalPages);
        quizCompletionPageable.setFirst(pageNo == 0);
        quizCompletionPageable.setLast(pageNo + 1 == totalPages);

        log.info("Current user id: {}", userId);
        return quizCompletionPageable;
    }

    private List<QuizCompletionDto> getAllQuizCompletionForUser(int userId, List<QuizCompletion> quizCompletions) {
        List<QuizCompletionDto> quizCompletionDto = this.quizCompletionAdapter.getQuizCompletionDtoFromQuizCompletions(quizCompletions);
        quizCompletionDto.removeIf(quizCompletion -> quizCompletion.getUserId() != userId);
        return quizCompletionDto;
    }

    @Override
    public QuizPageable getAllQuizzes(int pageNo, int offset, String sortedBy) {
        int totalSize = this.quizRepo.findAll().size();
        var quizPageable = new QuizPageable();
        Pageable pageable = PageRequest.of(pageNo, offset, Sort.by(sortedBy));
        int totalPages = (totalSize / offset) + 1;

        quizPageable.setPageable(pageable);
        quizPageable.setSort(Sort.by(sortedBy));
        quizPageable.setSize(offset);
        quizPageable.setTotalPages(totalPages);
        quizPageable.setFirst(pageNo == 0);
        quizPageable.setLast(pageNo + 1 == totalPages);
        quizPageable.setNumber(pageNo);
        quizPageable.setTotalElements(totalSize);

        Page<Quiz> pagedResult = quizRepo.findAll(pageable);
        if (pagedResult.hasContent()) {
            List<QuizDto> quizDto = this.quizAdapter.getListQuizDtoFromListQuiz(pagedResult.getContent());
            quizPageable.setEmpty(false);
            quizPageable.setContent(quizDto);
            quizPageable.setNumberOfElements(quizDto.size());
        } else {
            quizPageable.setEmpty(true);
            quizPageable.setContent(new ArrayList<>());
            quizPageable.setNumberOfElements(0);
        }
        return quizPageable;
    }
}
