package engine.paging_model;

import engine.dto.QuizCompletionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizCompletionPageable {
    private int totalPages;
    private int totalElements;
    private boolean last;
    private boolean first;
    private boolean empty;
    private List<QuizCompletionDto> content;
}
