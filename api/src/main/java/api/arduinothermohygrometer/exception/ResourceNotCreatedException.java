package api.arduinothermohygrometer.exception;

public class ResourceNotCreatedException extends RuntimeException {
    public ResourceNotCreatedException(final String message) {
        super(message);
    }
}
