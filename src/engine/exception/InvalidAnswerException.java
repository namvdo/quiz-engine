package engine.exception;

public class InvalidAnswerException extends Exception {
    private static final long serialVersionUID = 3387516993124229948L;

    public InvalidAnswerException(String msg) {
        super(msg);
    }
}
