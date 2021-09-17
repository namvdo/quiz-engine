package engine.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="answer")
public class QuizAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonIgnore
    private int answer;

    public QuizAnswer(int answer) {
        this.answer = answer;
    }

    @JsonProperty("answer")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public int getAnswer() {
        return answer;
    }
}
