package engine.exception;

public class QuizNotFoundException extends Exception{
    private static final long serialVersionUID = 3387516993124229948L;

    public QuizNotFoundException(String msg) {
        super(msg);
    }
}
