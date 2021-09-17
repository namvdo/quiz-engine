package engine.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity()
public class Quiz {

    @Id
    @TableGenerator(name="id_gen", allocationSize = 1, initialValue = -1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "id_gen")
    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    private String text;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<QuizOption> options;

    @JsonProperty("options")
    @Size(min = 2)
    public List<String> getOptions() {
        if (options == null) {
            return new ArrayList<>();
        }
        return options.stream().map(QuizOption::getOption).collect(Collectors.toList());
    }

    public void setOptions(List<String> quizOptions) {
        this.options = quizOptions.stream()
                .map(QuizOption::new)
                .collect(Collectors.toList());
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<QuizAnswer> answer;


    @JsonProperty(value = "answer", access = JsonProperty.Access.WRITE_ONLY)
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    public List<Integer> getAnswer() {
        if (answer == null) {
            return new ArrayList<>();
        }
        return answer.stream().map(QuizAnswer::getAnswer).collect(Collectors.toList());
    }


    public void setAnswer(List<Integer> answers) {
        this.answer = answers.stream()
                .map(QuizAnswer::new)
                .collect(Collectors.toList());
    }

    @Column(name = "user_id")
    @JsonIgnore
    private int userId;

}

