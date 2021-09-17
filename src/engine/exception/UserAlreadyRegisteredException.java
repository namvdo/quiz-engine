package engine.exception;

public class UserAlreadyRegisteredException extends Exception {
    private static final long serialVersionUID = -3387516993124229948L;

    public UserAlreadyRegisteredException(String msg) {
        super(msg);
    }
}
