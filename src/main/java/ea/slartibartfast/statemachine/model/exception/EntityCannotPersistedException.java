package ea.slartibartfast.statemachine.model.exception;

public class EntityCannotPersistedException extends RuntimeException {

    public EntityCannotPersistedException() {
        super();
    }

    public EntityCannotPersistedException(String message) {
        super(message);
    }

    public EntityCannotPersistedException(String message, Throwable cause) {
        super(message, cause);
    }
}
