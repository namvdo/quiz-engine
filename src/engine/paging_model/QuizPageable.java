package engine.paging_model;

import engine.dto.QuizDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizPageable {
    private int totalPages;
    private int totalElements;
    private boolean first;
    private boolean last;
    private Sort sort;
    private int number;
    private int numberOfElements;
    private int size;
    private boolean empty;
    private Pageable pageable;
    private List<QuizDto> content;
}
